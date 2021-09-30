git add .

set mydate=%date:/=%
set mytime=%time::=%
set mytimestamp=%mydate: =_%_%mytime:.=_%

git commit -m "autmatic upload at %mytimestamp%"
git push