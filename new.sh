#!/bin/bash
# usage: ./new.sh 1068
mkdir -p "$1"
cp templates/Template.java "$1/Main.java"
cd "$1" && nvim Main.java
