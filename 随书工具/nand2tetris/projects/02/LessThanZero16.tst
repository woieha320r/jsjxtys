load LessThanZero16.hdl,
output-file LessThanZero16.out,
compare-to LessThanZero16.cmp,
output-list in%B1.16.1 out%B2.1.2;

set in %B0100000000000000,
eval,
output;

set in %B1000000000000000,
eval,
output;