load IfZero16.hdl,
output-file IfZero16.out,
compare-to IfZero16.cmp,
output-list in%B1.16.1 isZero%B4.1.3 out%B1.16.1;

set in %B0101010101010101,
set isZero 1;
eval,
output;

set in %B1010101010101010,
set isZero 0;
eval,
output;