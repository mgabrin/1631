#!bin/bash

./runserver.sh &
./runInitializer.sh & 
../Components/InterfaceServer/compile.sh &
../Components/InterfaceServer/compileInputProcessor.sh &
node ../Components/Middleware/gui_api.js &