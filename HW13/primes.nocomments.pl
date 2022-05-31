init(N) :- build_cell(N, 2).

composite(1).

build_cell(N, CELL) :- N < CELL * CELL, !.
build_cell(N, CELL) :- SQ is CELL * CELL, build_composites(N, CELL, SQ),
    NEXT is CELL + 1, build_cell(N, NEXT), !.

build_composites(N, CELL, CUR) :- N < CUR, !.
build_composites(N, CELL, CUR) :- N >= CUR, assert(composite(CUR)),
    NEXT is CUR + CELL, build_composites(N, CELL, NEXT).

prime(N) :- \+ composite(N).

construct_number(R, D, []) :- R is D.
construct_number(1, _, []) :- !.
construct_number(R, D1, [D2 | DS]) :- prime(D1), D1 =< D2, construct_number(R1, D2, DS), R is D1 * R1.

construct_divisors(N, _, [N]) :- prime(N), !.
construct_divisors(N, D, [D | DS]) :- 0 is N mod D, NNEXT is div(N, D), construct_divisors(NNEXT, D, DS), !.
construct_divisors(N, D, DS) :- \+ (0 is N mod D), DNEXT is D + 1, construct_divisors(N, DNEXT, DS).

prime_divisors(1, []) :- !.
prime_divisors(R, [D | DS]) :- \+ integer(R), construct_number(R, D, DS), !.
prime_divisors(N, R) :- integer(N), construct_divisors(N, 2, R).

merge([], X, X) :- !.
merge([A | T1], [A | T2], [A | R]) :- merge(T1, T2, R), !.
merge([A | T1], [B | T2], [A | R]) :- A < B, merge(T1, [B | T2], R), !.
merge(A, B, R) :- merge(B, A, R).

lcm(A, B, R) :- prime_divisors(A, AD), prime_divisors(B, BD), merge(AD, BD, DS), prime_divisors(R, DS).
