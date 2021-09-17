set mypath=%cd% 
for /d /r %mypath% %%a in (build\) do if exist "%%a" rmdir /s /q "%%a"
for /d /r %mypath% %%a in (.gradle\) do if exist "%%a" rmdir /s /q "%%a"