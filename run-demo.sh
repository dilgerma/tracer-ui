#!/bin/bash

gradle bootRun -Dserver.port=$1 -DtargetUri=localhost:$2 -Dspring.application.name=tracer-\$\{server.port\}
