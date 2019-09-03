#!/bin/bash

killall node
git stash
git pull
git stash pop
npm install
node bin/www >> node.log 2>&1 &
exit
