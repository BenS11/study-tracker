# Commands

<small>(* denotes unimplemented commands)</small>

## begin

Starts a new active study session.

### Arguments
- `subject` (optional): Subject being studied.
- `-d, --date` (optional): Session start date.
- `-t, --time` (optional): Session start time.

## end

Ends an active study session.

### Arguments
- `-s, --subject` (optional): Subject of the session to end.

## record

Manually records a completed study session.

### Arguments
- `-d, --date-time`: Session date.
- `-s, --subject`: Subject studied.
- `-t, --time`: Session start time.
- `-e, --end-time`: Session end time.
- `-s, --subject`: Subject studied.

## display

Displays active or completed study sessions.

### Arguments
- `-a, --active`: Display active sessions.
- `-c, --complete`: Display completed sessions.
- `-s, --start-range`: start of range to display
- `-e, --end-range`: end of range to display

## delete

Deletes a completed study session.

### Arguments
- `id`: ID of the session to delete.

## summary

Displays general study statistics.

### Arguments
- `-s, --start-date`: Start of range to summarize
- `-e, --end-date`: Start of range to summarize
- `-a, --all`: Summarize all sessions 

### Statistics
- Total study time.
- Average session duration.
- Average time spent per day.
- Most-studied subject.
- Time spent on most studied subject.
- more to be added
## streak*

Displays study consistency statistics.

### Arguments
- `--subject`: Filter streak statistics by subject.

### Statistics
- Current study streak.
- Longest study streak.
- Total days studied.
- Average study days per week.
- Most recent study date.

## subjects*

Lists subjects that have been studied.

### Arguments
- `--sort`: Sort subjects by name or total study time.

## reset

Deletes all stored study session data.

### Arguments
- `-f`, `--force`: Skip confirmation (required to reset).

## help

Displays usage instructions.

### Arguments
- `command`: Optional command name to display specific usage instructions.
