# Grep CLI
This tool allows for aggregated search of log access log files using regular expressions and date filtering ranges.
It also supports output of results to file. Usage information can be display by running with no arguments or `-h`.

## Usage
```bash
$ java -jar GrepCLI-fat-1.0-SNAPSHOT.jar -h
Usage: Log Grep options_list
Options:
    --input, -i -> Input directory (always required) { String }
    --query, -q -> Query regular expression (always required) { String }
    --output, -o -> Output file name { String }
    --from, -f -> Datetime to filter from (dd/MMM/yyyy:HH:mm:ss Z) { String }
    --to, -t -> Datetime to filter to (dd/MMM/yyyy:HH:mm:ss Z) { String }
    --numResults, -n [10] -> Number of results to display { Int }
    --help, -h -> Usage info
```

Example Query
```bash
$ java -jar GrepCLI-fat-1.0-SNAPSHOT.jar -i tmp/NASA_access_log/ -q unicomp[0-9] -f "01/Jul/1995:00:00:09 -0400" -t "03/Jul/1995:21:41:16 -0400" -n 20 -o results.log
Loading log tmp\NASA_access_log\August_1995\nasa_august_1995.log
Loading log tmp\NASA_access_log\July_1995\nasa_july_1995.log
Loaded logs in 973ms
Found 17 matches for "unicomp[0-9]" from: 1995-07-01T00:00:09  to: 1995-07-03T21:41:16 in 6055ms
unicomp6.unicomp.net - - [01/Jul/1995:00:00:14 -0400] "GET /shuttle/countdown/count.gif HTTP/1.0" 200 40310
unicomp6.unicomp.net - - [01/Jul/1995:00:00:14 -0400] "GET /images/NASA-logosmall.gif HTTP/1.0" 200 786
unicomp6.unicomp.net - - [01/Jul/1995:00:00:14 -0400] "GET /images/KSC-logosmall.gif HTTP/1.0" 200 1204
unicomp6.unicomp.net - - [01/Jul/1995:00:01:41 -0400] "GET /htbin/cdt_main.pl HTTP/1.0" 200 3214
unicomp6.unicomp.net - - [01/Jul/1995:00:02:17 -0400] "GET /facilities/lcc.html HTTP/1.0" 200 2489
unicomp6.unicomp.net - - [01/Jul/1995:00:02:20 -0400] "GET /images/ksclogosmall.gif HTTP/1.0" 200 3635
unicomp6.unicomp.net - - [01/Jul/1995:00:02:21 -0400] "GET /images/kscmap-tiny.gif HTTP/1.0" 200 2537
unicomp6.unicomp.net - - [01/Jul/1995:00:03:09 -0400] "GET /images/lcc-small2.gif HTTP/1.0" 200 58026
unicomp6.unicomp.net - - [01/Jul/1995:00:04:16 -0400] "GET /ksc.html HTTP/1.0" 200 7074
unicomp6.unicomp.net - - [01/Jul/1995:00:04:19 -0400] "GET /images/ksclogo-medium.gif HTTP/1.0" 200 5866
unicomp6.unicomp.net - - [01/Jul/1995:00:04:26 -0400] "GET /images/USA-logosmall.gif HTTP/1.0" 200 234
unicomp6.unicomp.net - - [01/Jul/1995:00:04:26 -0400] "GET /images/WORLD-logosmall.gif HTTP/1.0" 200 669
unicomp6.unicomp.net - - [01/Jul/1995:00:04:27 -0400] "GET /images/MOSAIC-logosmall.gif HTTP/1.0" 200 363
unicomp5.unicomp.net - - [03/Jul/1995:13:39:06 -0400] "GET /shuttle/ HTTP/1.0" 200 957
unicomp5.unicomp.net - - [03/Jul/1995:13:39:27 -0400] "GET /shuttle/countdown/ HTTP/1.0" 200 3985
unicomp3.unicomp.net - - [03/Jul/1995:21:40:17 -0400] "GET /shuttle/missions/sts-71/images/images.html HTTP/1.0" 200 7634
unicomp3.unicomp.net - - [03/Jul/1995:21:41:16 -0400] "GET /shuttle/missions/sts-71/images/KSC-95EC-0917.txt HTTP/1.0" 200 1392
Writing results to file  results.log
```

## Building
To build gradle project just execute the build task using the wrapper.

```bash
./gradlew build
```

This will compile and build jars. To use as a command line executable use the generated 'fat' jar. It should look something like this under `build/libs/`
```
GrepCLI-fat-1.0-SNAPSHOT.jar
```

If you're running from IDE such as Intellij just create a run configuration using the `main` function in `Main.Kt`.
Then provide program arguments in the configuration.

Example program arguments for runtime configuration in IDE.
```aidl
-i /tmp/NASA_access_log/ -q unicomp[0-9] -f "01/Jul/1995:00:00:09 -0400" -t "03/Jul/1995:21:41:16 -0400"
```

## Log Data
You need to provide the CLI with a path to the directory containing `*.log` files. The CLI will recursively walk this directory selecting matching files to load.
An example layout might look like this.
```aidl
NASA_access_log/

  July_1995/

    nasa_july_1995.log

  August_1995/

    nasa_august_1995.log
```