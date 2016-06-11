@echo off
:: Launch Lint4j on a Windows system.
:: Adapted from the findbugs team
:: This will only work on Windows NT or later!

:: Don't affect environment outside of this invocation
setlocal

:: ----------------------------------------------------------------------
:: Set up default values
:: ----------------------------------------------------------------------
set appjar=lint4j.jar
set javahome=
set launcher=java.exe
set start=
set jvmargs=
set args=
set javaProps=
set maxheap=256

:: Try finding the default LINT4J_HOME directory
:: from the directory path of this script
set default_lint4j_home=%~dp0..

:: Honor JAVA_HOME environment variable if it is set
if "%JAVA_HOME%"=="" goto nojavahome
if not exist "%JAVA_HOME%\bin\javaw.exe" goto nojavahome
set javahome=%JAVA_HOME%\bin\
:nojavahome

goto loop

:: ----------------------------------------------------------------------
:: Process command-line arguments
:: ----------------------------------------------------------------------

:shift2
shift
:shift1
shift

:loop

:: Remove surrounding quotes from %1 and %2
set firstArg=%~1
set secondArg=%~2

if "%firstArg%"=="-home" set LINT4J_HOME=%secondArg%
if "%firstArg%"=="-home" goto shift2

if "%firstArg%"=="-J" set jvmargs=%secondArg%
if "%firstArg%"=="-J" goto shift2

if "%firstArg%"=="-maxHeap" set maxheap=%secondArg%
if "%firstArg%"=="-maxHeap" goto shift2

if "%firstArg%"=="-javahome" set javahome=%secondArg%\bin\
if "%firstArg%"=="-javahome" goto shift2

if "%firstArg%"=="-property" set javaProps=-D%secondArg% %javaProps%
if "%firstArg%"=="-property" goto shift2

if "%firstArg%"=="-help" goto help

if "%firstArg%"=="" goto launch

set args=%args% "%firstArg%"
goto shift1

:: ----------------------------------------------------------------------
:: Launch Lint4j
:: ----------------------------------------------------------------------
:launch
:: Make sure LINT4J_HOME is set.
:: If it isn't, try using the default value based on the
:: directory path of the invoked script.
:: Note that this will fail miserably if the value of LINT4J_HOME
:: has quote characters in it.
if not "%LINT4J_HOME%"=="" goto checkHomeValid
set LINT4J_HOME=%default_lint4j_home%

:checkHomeValid
if not exist "%LINT4J_HOME%\jars\%appjar%" goto homeNotSet

:found_home
:: Launch Lint4j!
%start% "%javahome%%launcher%" %javaProps% -Xmx%maxheap%m %jvmargs% -jar "%LINT4J_HOME%\jars\%appjar%" %args%
goto end

:: ----------------------------------------------------------------------
:: Display usage information
:: ----------------------------------------------------------------------
:help
echo Usage: lint4j [options]
echo    -home dir       Use dir as LINT4J_HOME
echo    -J args         Pass args to JVM
echo    -maxHeap size   Set maximum Java heap size in megabytes (default %maxheap%)
echo    -javahome dir   Specify location of JRE
echo    -help           Display this message
echo All other options are passed to the Lint4j application
goto end

:: ----------------------------------------------------------------------
:: Report that LINT4J_HOME is not set (and was not specified)
:: ----------------------------------------------------------------------
:homeNotSet
echo Could not find Lint4j home directory.  There may be a problem with the Lint4j installation.
echo Try setting LINT4J_HOME, 
goto end

:end
