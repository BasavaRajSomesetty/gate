#!/bin/bash

${WORK_DIR}/gate/bin/gate | tee ${WORK_DIR}/logs/gate.log

cd ${WORK_DIR}
sleep 10

while :
do
    sleep 100
    # For Debugging, Docker should alive!
done

echo "gate services started ..."
