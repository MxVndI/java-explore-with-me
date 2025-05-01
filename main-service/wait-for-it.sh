#!/usr/bin/env sh

WAITFORIT_cmdname=$(basename "$0")

echoerr() {
    if [ "$WAITFORIT_QUIET" -ne 1 ]; then
        echo "$@" 1>&2
    fi
}

usage() {
    cat << USAGE >&2
Usage:
    $WAITFORIT_cmdname host:port [-s] [-t timeout] [-- command args]
    -h HOST | --host=HOST       Host or IP under test
    -p PORT | --port=PORT       TCP port under test
    -s | --strict               Only execute subcommand if the test succeeds
    -q | --quiet                Don't output any status messages
    -t TIMEOUT | --timeout=TIMEOUT
                                Timeout in seconds, zero for no timeout
    -- COMMAND ARGS             Execute command with args after the test finishes
USAGE
    exit 1
}

wait_for() {
    if [ "$WAITFORIT_TIMEOUT" -gt 0 ]; then
        echoerr "$WAITFORIT_cmdname: waiting $WAITFORIT_TIMEOUT seconds for $WAITFORIT_HOST:$WAITFORIT_PORT"
    else
        echoerr "$WAITFORIT_cmdname: waiting for $WAITFORIT_HOST:$WAITFORIT_PORT without a timeout"
    fi

    WAITFORIT_start_ts=$(date +%s)
    while true; do
        nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT"
        WAITFORIT_result=$?
        if [ $WAITFORIT_result -eq 0 ]; then
            WAITFORIT_end_ts=$(date +%s)
            echoerr "$WAITFORIT_cmdname: $WAITFORIT_HOST:$WAITFORIT_PORT is available after $((WAITFORIT_end_ts - WAITFORIT_start_ts)) seconds"
            break
        fi
        sleep 1
    done
    return $WAITFORIT_result
}

# Обработка аргументов
WAITFORIT_HOST=""
WAITFORIT_PORT=""
WAITFORIT_TIMEOUT=15
WAITFORIT_STRICT=0
WAITFORIT_QUIET=0
WAITFORIT_CLI=""

while [ $# -gt 0 ]; do
    case "$1" in
        *:* )
            WAITFORIT_HOST=$(echo "$1" | cut -d ':' -f 1)
            WAITFORIT_PORT=$(echo "$1" | cut -d ':' -f 2)
            shift 1
            ;;
        --strict)
            WAITFORIT_STRICT=1
            shift 1
            ;;
        -q|--quiet)
            WAITFORIT_QUIET=1
            shift 1
            ;;
        -t|--timeout)
            if echo "$1" | grep -q "="; then
                WAITFORIT_TIMEOUT=$(echo "$1" | cut -d '=' -f 2)
                shift 1
            else
                WAITFORIT_TIMEOUT="$2"
                shift 2
            fi
            ;;
        --timeout=*)
            WAITFORIT_TIMEOUT=$(echo "$1" | cut -d '=' -f 2)
            shift 1
            ;;
        --)
            shift
            WAITFORIT_CLI="$@"
            break
            ;;
        --help)
            usage
            ;;
        *)
            echoerr "Unknown argument: $1"
            usage
            ;;
    esac
done

if [ -z "$WAITFORIT_HOST" ] || [ -z "$WAITFORIT_PORT" ]; then
    echoerr "Error: you need to provide a host and port to test."
    usage
fi

wait_for

if [ -n "$WAITFORIT_CLI" ]; then
    if [ $WAITFORIT_STRICT -eq 1 ] && [ $WAITFORIT_result -ne 0 ]; then
        echoerr "$WAITFORIT_cmdname: strict mode, refusing to execute subprocess"
        exit $WAITFORIT_result
    fi
    exec $WAITFORIT_CLI
else
    exit $WAITFORIT_result
fi