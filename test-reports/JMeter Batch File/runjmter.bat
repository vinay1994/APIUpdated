CLS
@ECHO OFF
ECHO.
FOR /f "tokens=2-4 delims=/ " %%a IN ('DATE /t') DO (SET testDate12=%%c-%%a-%%b)
FOR /f "tokens=1-2 delims=/:" %%a IN ('TIME /t') DO (SET testTime12=%%a:%%b)
FOR /f "tokens=2-4 delims=/ " %%a IN ('DATE /t') DO (SET testDate24=%%b_%%a_%%c)
FOR /f "tokens=1-2 delims=/:" %%a IN ("%TIME%") DO (SET testTime24=%%a_%%b)
TITLE [MSG@ND] API Testing With JMeter ON %testDate12% %testTime12%
ECHO [MSG@ND] Welcome to JMeter scrip run utility... && ECHO [MSG@ND] Enter all drive path and file names correctly to get proper test run reports...
PAUSE
ECHO.
SET /p jmtrDirPath="Enter Drive Letter Other Than "C" For Logs [eg. E, F, etc.]: "
IF "%jmtrDirPath%"=="" GOTO Error
ECHO.
SET /p jmtrScriptPath="Enter API Test Script Absolute File Path [eg. %jmtrDirPath%:\API_Test\testscript.jmx]: "
IF "%jmtrScriptPath%"=="" GOTO Error
::ECHO.
::SET /p jmtrLogPath="Enter JMeter Run Log File Path [eg. %jmtrDirPath%:\API_Test\testlog.txt]: "
::IF "%jmtrLogPath%"=="" GOTO Error
::ECHO.
::SET /p jmtrResultsPath="Enter JMeter Results File Path [eg. %jmtrDirPath%:\API_Test\testresults.txt]: "
::IF "%jmtrResultsPath%"=="" GOTO Error
SET jmtrLogPath=%jmtrDirPath%:\JmeterLog_%testDate24%_%testTime24%.txt
SET jmtrResultsPath=%jmtrDirPath%:\JmeterResult_%testDate24%_%testTime24%.txt
ECHO.
SET /p jmtrBinPath="Enter Absolute JMeter bin Directory Path [eg. %jmtrDirPath%:\Apache\jmeter3.0\bin]: "
IF "%jmtrBinPath%"=="" GOTO Error
::IF "%jmtrLogPath%"=="" (
::	SET jmtrLogPath=%jmtrDirPath%:\JmeterLog_%testDate24%_%testTime24%.txt
::	@ECHO OFF %jmtrLogPath%
::	)
::IF "%jmtrResultsPath%"=="" (
::	SET jmtrResultsPath=%jmtrDirPath%:\JmeterResult_%testDate24%_%testTime24%.txt
::	@ECHO OFF %jmtrResultsPath%
::	)
ECHO.
ECHO Entered JMeter bin Directory Path: %jmtrBinPath% && ECHO Entered API Test Script File Path: %jmtrScriptPath%
ECHO Your JMeter Run Log File Path: %jmtrLogPath% && ECHO Your JMeter Results File Path: %jmtrResultsPath%
::CD %jmtrBinPath%
::%jmtrDirPath%:
ECHO.
ECHO Running API Test of script %jmtrScriptPath%.........
ECHO.
::jmeter -n -t %jmtrScriptPath% -l %jmtrLogPath% >> %jmtrResultsPath%
%jmtrBinPath%\jmeter -n -t %jmtrScriptPath% -l %jmtrLogPath% >> %jmtrResultsPath%
:Error
ECHO Oops!! Script execution error. This time you are exhausted. Bye bye!!
PAUSE
::GOTO End
:::End