#!/bin/bash
kill -s 9 `ps aux | grep BootFlux.jar | awk '{print $2}'`
