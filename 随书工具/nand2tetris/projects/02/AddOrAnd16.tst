load AddOrAnd16.hdl,
output-file AddOrAnd16.out,
compare-to AddOrAnd16.cmp,
output-list a%B1.16.1 b%B1.16.1 isAdd%B3.1.3 out%B1.16.1;

set a %B0101010101010101,
set b %B0101110101010101,
set isAdd 1,
eval,
output;

set a %B0010101010101010,
set b %B0011010101010101,
set isAdd 0,
eval,
output;