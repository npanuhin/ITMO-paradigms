%% WARNING: poor code style

get_size(null, 0) :- !.
get_size(node(Key, Value, Priority, Size, Left, Right), Size).
node_gen(Key, Value, Priority, Left, Right, node(Key, Value, Priority, Size, Left, Right)) :- get_size(Left, SL),
                                                                                              get_size(Right, SR),
                                                                                              Size is SL + SR + 1.

merge(null, T2, T2) :- !.
merge(T1, null, T1) :- !.
merge(node(K1, V1, P1, S1, L1, R1), node(K2, V2, P2, S2, L2, R2), Result) :- P1 > P2, !,
                                                                    merge(R1, node(K2, V2, P2, S2, L2, R2), RES_R),
                                                                    node_gen(K1, V1, P1, L1, RES_R, Result).
merge(node(K1, V1, P1, S1, L1, R1), node(K2, V2, P2, S2, L2, R2), Result) :-
                                                                    merge(node(K1, V1, P1, S1, L1, R1), L2, RES_L),
                                                                    node_gen(K2, V2, P2, RES_L, R2, Result).

split(null, _, null, null, null) :- !.
split(node(K, V, P, S, L, R), K, L, node(K, V, P, 1, null, null), R) :- !.
split(node(K, V, P, S, L, R), Key, TL, TM, TR) :- K < Key, !,
                                                  split(R, Key, RES_TLR, TM, TR),
                                                  node_gen(K, V, P, L, RES_TLR, TL).

split(node(K, V, P, S, L, R), Key, TL, TM, TR) :- split(L, Key, TL, TM, TR_NEW),
                                                  node_gen(K, V, P, TR_NEW, R, TR).

map_put(TreeMap, Key, Value, Result) :- rand_int(2147483647, P),
                                        split(TreeMap, Key, TL, TM, TR),
                                        merge(TL, node(Key, Value, P, 1, null, null), RES_TL),
                                        merge(RES_TL, TR, Result).

map_build([], null) :- !.
map_build([(K, V) | L], Result) :- map_build(L, T),
                                   map_put(T, K, V, Result).

map_get(node(K, V, P, S, L, R), K, V) :- !.
map_get(node(K, V, P, S, L, R), Key, Value) :- Key < K, !, map_get(L, Key, Value).
map_get(node(K, V, P, S, L, R), Key, Value) :- map_get(R, Key, Value).

map_remove(TreeMap, Key, Result) :- split(TreeMap, Key, TL, TM, TR), merge(TL, TR, Result).

map_subMapSize(MTreeMapap, FromKey, ToKey, 0) :- FromKey >= ToKey, !.
map_subMapSize(TreeMap, FromKey, ToKey, Result) :- split(TreeMap, FromKey, TL1, TM1, TR1),
                                                   split(TR1, ToKey, TL2, TM2, TR2),
                                                   get_size(TM1, S1),
                                                   get_size(TL2, S2),
                                                   Result is S1 + S2.
