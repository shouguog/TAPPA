@echo off
goto CMD

***********************************************************************
Copyright @ 1999-2003, The Institute for Genomic Research (TIGR).
All rights reserved.
***********************************************************************
 $RCSfile$
 $Revision$
 $Date$
 $Author$
 $State$
***********************************************************************

:CMD

set ClassPath=pathway.jar;javaLib/jxl.jar;javaLib/javastat.zip;
java -Xmx512m -cp %ClassPath% edu.mcw.mcgee.MainFrame