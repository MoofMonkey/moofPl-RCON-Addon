# moofPl-RCON-Addon

To use:
new moofPl.rcon.RCON (
  moofPl.rcon.RCONUtils.parseUsers (
    new File (
      "accs.txt"
    )
  )
);

accs.txt format:
login[TAB]pass[TAB]isSuperUser(true/false)
