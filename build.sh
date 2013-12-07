#!/bin/bash

cp -r ./natives/mac32/*.* ./builds/mac32
cp -r ./source/engine/Models/ ./builds/mac32/Models/
cp -r ./source/engine/Shaders/ ./builds/mac32/Shaders/
cp -r ./source/engine/Texture/ ./builds/mac32/Texture/
cp -r ./source/engine/bin/sagma/ ./builds/mac32/sagma/
cp FileGetter.class ./builds/mac32/sagma/core/io/
cp ./source/engine/changelog.txt ./builds/mac32/