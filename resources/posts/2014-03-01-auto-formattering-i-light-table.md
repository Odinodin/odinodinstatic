:title Auto-formattering i Light Table
:published 2014-03-01
:body

[Light Table](http://www.lighttable.com/) er en fascinerende [open source](https://github.com/LightTable/LightTable) editor med en arkitektur som gjør den lett å tilpasse og utvide. 

For å gi et konkret eksempel på hva det innebærer, så skal jeg vise hvordan man kan lage sin egen auto-formatteringskommando som vil fikse formatteringen på all teksten i en fil.

Auto-formattering er noe som følger med i Light Table, men denne forutsetter at man har selektert tekst før man kjører kommandoen. 

Så det jeg vil gjøre er å: 

1.  Selektere all tekst i en fil
2.  Kjøre "Smart indent lines"
3.  Deselektere teksten 

For å få til dette åpner vi 'user.keymap' og legger til følgende oppførsel til tastatursnarveien 'Cmd + alt + l'.

```clj
{:+ {:editor {
              "pmeta-alt-l" [:editor.select-all 
              				 :smart-indent-selection 
              				 :editor.selection.clear]}}}
```

Det som vil skje nå når du trykker 'Cmd + Alt + l' er at du fyrer av 3 kommandoer etter hverandre som til sammen vil autoformatere all tekst.