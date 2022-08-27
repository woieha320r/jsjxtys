load IfNot16.hdl,
output-file IfNot16.out,
compare-to IfNot16.cmp,
output-list in%B1.16.1 isNot%B3.1.3 out%B1.16.1;

set in %B0101010101010101,
set isNot 1;
eval,
output;

set in %B0010101010101010,
set isNot 0;
eval,
output;