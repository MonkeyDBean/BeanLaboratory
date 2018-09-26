#!/bin/bash
kill -s 9 `ps aux | grep BootFlux | awk '{print $2}'`