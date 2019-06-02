#!/bin/bash
cd ./bin
sudo service lightdm stop
java Main >~/Desktop/out.txt & disown

