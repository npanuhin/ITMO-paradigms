init(N) :- build_cell(N, 2).  %% Build the Sieve of Eratosthenes

composite(1).  %% N(= 1) is composite (storage)

build_cell(N, CELL) :- N < CELL * CELL, !.                                %% While CELL <= sqrt(N):
build_cell(N, CELL) :- SQ is CELL * CELL, build_composites(N, CELL, SQ),  %%   build_composites(N, CELL, CELL^2)
    NEXT is CELL + 1, build_cell(N, NEXT), !.                             %%   CELL += 1

build_composites(N, CELL, CUR) :- N < CUR, !.                        %% While CUR <= N:
build_composites(N, CELL, CUR) :- N >= CUR, assert(composite(CUR)),  %%   Set CUR as composite number
    NEXT is CUR + CELL, build_composites(N, CELL, NEXT).             %%   CUR += CELL

prime(N) :- \+ composite(N).  %% N is prime if N is not composite

construct_number(R, D, []) :- R is D.   %% The only divisor -> RES = this divisor
construct_number(1, _, []) :- !.        %% No divisors -> RES = 1
construct_number(R, D1, [D2 | DS]) :- prime(D1), D1 =< D2, construct_number(R1, D2, DS), R is D1 * R1.  %% RES = D1 * construct_number(D2, DS)

construct_divisors(N, _, [N]) :- prime(N), !.  %% The only divisor of the prime number is this number
%% N % D != 0 -> RES = construct_divisors(N, D + 1):
construct_divisors(N, D, DS) :- \+ (0 is N mod D), DNEXT is D + 1, construct_divisors(N, DNEXT, DS), !.
%% N % D == 0 -> RES = [D|construct_divisors(N / D, D)]:
construct_divisors(N, D, [D | DS]) :- 0 is N mod D, NNEXT is div(N, D), construct_divisors(NNEXT, D, DS).

prime_divisors(1, []) :- !.  %% 1 has no divisors | no divisors equals 1
prime_divisors(R, [D | DS]) :- \+ integer(R), construct_number(R, D, DS), !.  %% Divisors are given -> construct N
prime_divisors(N, R) :- integer(N), construct_divisors(N, 2, R).              %% N is given -> construct divisors

merge([], X, X) :- !.
%% merge([A|T1], [B|T2]) = ...
merge([A | T1], [A | T2], [A | R]) :- merge(T1, T2, R), !.               %% [A|merge(T1,T2)] if A = B
merge([A | T1], [B | T2], [A | R]) :- A < B, merge(T1, [B | T2], R), !.  %% [A|merge(T1,T2)] if A < B
merge(A, B, R) :- merge(B, A, R).                                        %% [B|merge(T1,T2)] if B < A (rec-call to A < B)

lcm(A, B, R) :- prime_divisors(A, AD), prime_divisors(B, BD), merge(AD, BD, DS), prime_divisors(R, DS).
%%              ↳ AD = divisors(A)     ↳ BD = divisors(B)     ↳ DS = AD + BD without repetition    ⤷ construct RES from divisors DS
