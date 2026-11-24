# Commands

<small>(* denotes unimplemented commands)</small>

## begin

Starts a new active study session.

### Arguments
- `subject` (optional): Subject being studied.
- `--date` (optional): Session start date.
- `--time` (optional): Session start time.

## end

Ends an active study session.

### Arguments
- `--subject` (optional): Subject of the session to end.

## record

Manually records a completed study session.

### Arguments
- `subject`: Subject studied.
- `--date`: Session date.
- `--start`: Session start time.
- `--end`: Session end time.
- `--duration`: Session duration.

## display

Displays active or completed study sessions.

### Arguments
- `--active`: Display active sessions.
- `--complete`: Display completed sessions.
- `--from`: Filter sessions starting on or after this date.
- `--to`: Filter sessions starting on or before this date.
- `--id`: Display a specific session by ID.
- `--subject`: Filter by subject.

## delete

Deletes a completed study session.

### Arguments
- `id`: ID of the session to delete.

## stats*

Displays general study statistics.

### Arguments
- `--subject`: Filter statistics by subject.
- `--from`: Beginning of the date range.
- `--to`: End of the date range.

### Statistics
- Total study time.
- Number of completed sessions.
- Average session duration.
- Longest session.
- Most-studied subject.
- Study time by subject.

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
- `-f`, `--force`: Skip confirmation.

## help

Displays usage instructions.

### Arguments
- `command`: Optional command name to display specific usage instructions.
