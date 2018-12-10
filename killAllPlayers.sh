#!/usr/bin/env bash

for pid in $(ps -ef | grep "java" | awk '{print $2}'); do kill -9 $pid; done