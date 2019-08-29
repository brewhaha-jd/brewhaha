#!/bin/bash

killall node
git pull
npm install
node bin/www >> node.log 2>&1 &
exit
