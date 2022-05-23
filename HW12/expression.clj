(load-file "proto.clj")

(defn abstractOperation [f] (fn [& args] (fn [vars] (apply f (map #(% vars) args)))))

(def constant constantly)
(defn variable [name] (fn [vars] (vars name)))
(def add (abstractOperation +))
(def subtract (abstractOperation -))
(def multiply (abstractOperation *))
(def negate (abstractOperation -))

(defn divide-imp
  ([arg] (/ 1 (double arg)))
  ([frst & args] (/ (double frst) (reduce * args))))
(def divide (abstractOperation divide-imp))

(defn sumexp-imp [& args] (reduce + (map #(Math/exp %) args)))
(def sumexp (abstractOperation sumexp-imp))

(defn softmax-imp [& args] (/ (Math/exp (first args)) (apply sumexp-imp args)))
(def softmax (abstractOperation softmax-imp))

(def OperantionFunctions {
  '+       add
  '-       subtract
  '*       multiply
  '/       divide
  'negate  negate
  'sumexp  sumexp
  'softmax softmax
  })

(defn parse [operationType const-imp var-imp expr]
  (cond
    (seq? expr)    (apply (operationType (first expr)) (map #(parse operationType const-imp var-imp %) (rest expr)))
    (number? expr) (const-imp (double expr))
    (symbol? expr) (var-imp (str expr))))

(defn parseFunction [expression] (parse OperantionFunctions constant variable (read-string expression)))


; ------------------------------ HW 11 ------------------------------

(defn diff [expr target]
  ((proto-get expr :diff) expr target))
(defn toString [expr]
  ((proto-get expr :toString) expr))
(defn evaluate [expr args]
  ((proto-get expr :evaluate) expr args))

(def operands (fn [this] (proto-get this :operands)))

(defn AbstractOperationPrototype [f sign f_diff] {
  :diff f_diff
  :toString (fn [this] (str "(" sign " " (clojure.string/join " " (mapv toString (this :operands))) ")"))
  :evaluate (fn [this args] (apply f (mapv #(evaluate % args) (this :operands))))
  })

(defn AbstractOperation [f sign f_diff]
  (let [this {
    :prototype (AbstractOperationPrototype f sign f_diff)
    }]
    (fn [& args] (assoc this :operands args))))

(defn Constant [x] {
  :diff     (fn [this target] (Constant 0))
  :evaluate (fn [this args] x)
  :toString (fn [& args] (format "%.01f" (double x)))
  })

(defn Variable [x] {
  :diff     (fn [this target] (if (identical? target x) (Constant 1) (Constant 0)))
  :evaluate (fn [this args] (get args x))
  :toString (fn [& args] (str x))
  })

(def Add (AbstractOperation + "+" (fn [this target]
  (apply Add (map #(diff % target) (operands this))))))

(def Subtract (AbstractOperation - "-" (fn [this target]
  (apply Subtract (map #(diff % target) (operands this))))))

(def Multiply (AbstractOperation * "*" (fn [this target]
  (apply Add (mapv
    (fn [x] (apply Multiply (mapv (fn [y] (if (identical? x y) (diff x target) y)) (operands this))))
    (operands this))))))

(def Negate (AbstractOperation - "negate" (fn [this target]
  (let [x (first (operands this))]
    (Negate (diff x target))))))

(defn single-elem? [s] (and (seq s) (empty? (rest s))))
(def Divide (AbstractOperation divide-imp "/" (fn [this target]
  (if (single-elem? (operands this))
    (let [x (first (operands this))]
      (Divide (Negate (diff x target)) (Multiply x x)))

    (let [args (operands this)]
      (apply Subtract (for [i (range (count args))]
        (Divide
          (Multiply (first args) (diff (nth args i) target))
          (Multiply (nth args i) (apply Multiply (rest args)))))))))))

(def Exp (AbstractOperation #(Math/exp %) nil (fn [this target]
  (let [x (first (operands this))]
    (Multiply (diff x target) (Exp x))))))

(def Sumexp (AbstractOperation sumexp-imp "sumexp" (fn [this target]
  (let [args (operands this)]
    (apply Add (for [i (range (count args))]
      (diff (Exp (nth args i)) target)))))))

(def Softmax (AbstractOperation softmax-imp "softmax" (fn [this target]
  (let [args (operands this)]
    (diff (Divide (Exp (first args)) (apply Sumexp args)) target)))))

(def OperationObjects {
  '+       Add
  '-       Subtract
  '*       Multiply
  '/       Divide
  'negate  Negate
  'sumexp  Sumexp
  'softmax Softmax
  })

(defn parseObject [expr] (parse OperationObjects Constant Variable (read-string expr)))



; Combinatorial parser


(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))


(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))

(defn +char-not [chars] (_char (comp not (set chars))))

(defn +map [f parser] (comp (partial _map f) parser))

(def +parser _parser)

(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))

(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))

(defn +string [st] (apply +seq (mapv #(+char (str %) )st)))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))

(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))

(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))

(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+plus *digit))))
(def *double (+map read-string (+seqf str (+opt (+char "-")) *number (+opt (+seqf str (+char ".") *number)))))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(defn or_string [args] (apply +or
                              (mapv #(+string (str %))  args)))

(def OPERS [
            ; [{
            ;   '** Pow
            ;   (symbol "//") Log} 0]
            [{
             '* Multiply
             '/ Divide} 1]
            [{
              '+ Add
             '- Subtract} 1]])

(def UNARY {
               'negate Negate})

(defn setOpers[maps] (+seqf  #(get maps (symbol (apply str %))) (or_string (keys maps))))

(def *Unary (setOpers UNARY))
(def *Vars (+seqf #(Variable (apply str %)) (or_string ['x 'y 'z])))
(def *Const (+seqf #(Constant %) *double))
(def *Ones (+or *Const *Vars ))
(def *arg (+seqf identity *ws *Ones *ws))
(defn left[a b]  (if (empty? b) a
                             (reduce #((first %2) %1 (last %2)) (concat [a] b))))
(defn right[a b] (if (empty? b) a
                             (letfn [(f[op b a] (op a b))]
                               ((reduce #(partial f (first %2) (%1 (last %2))) (concat [identity] (reverse b))) a))))
(defn unary[a b] (a b))
(defn after[op arg] (+star (+seqf (fn[& a] (flatten a)) *ws op arg)))

(declare *expr)

(def *base (+or *arg
             (+seqf unary *ws *Unary *ws (delay *base))
             (+seqn 1 (+seq *ws (+char "(") *ws) (delay *expr) (+seq *ws (+char ")") *ws))))
(def ASSOCIATES {0 right
                 1 left})

(defn setLevels[n] (if (= n -1) *base
                               (let [n' (dec n)
                                     level (nth OPERS n)
                                     associate (get ASSOCIATES (last level))
                                     *Opers (setOpers (first level))
                                     *next (setLevels n')]
                                 (+seqf associate *next (after *Opers *next)))))

(def *expr (setLevels 1))


(defn parseObjectInfix[expr] ((_parser *expr) expr))
