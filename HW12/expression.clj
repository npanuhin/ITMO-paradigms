(definterface IOperation
              (^Number evaluate [cur_vars])
              (^String toString [])
              (^String toStringInfix [])
              (diff [cur_var]))

(declare ZERO)
(declare ONE)

(deftype abstractTerminal[evaluate toString diff value]
         IOperation
         (evaluate [this cur_vars] (evaluate value cur_vars))
         (toString [this] (toString value))
         (toStringInfix [this] (toString value))
         (diff [this cur_var] (diff value cur_var)))

(defn createTerminal [evaluate toString diff]
      #(abstractTerminal. evaluate toString diff %))

(def Constant
  (createTerminal
    (fn [a b] a)
    #(format "%.1f" (double %))
    (fn [a b] ZERO)))

(def Variable
  (createTerminal
    #(%2 %1)
    (fn [a] a)
    (fn [a b] (if (= a b) ONE ZERO))))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(deftype abstractExpression [func sign dff args]
         IOperation
         (evaluate [this cur_vars] (apply func (mapv #(.evaluate % cur_vars) args)))
         (toString [this] (str "(" sign " " (clojure.string/join " " (mapv #(.toString %) args)) ")"))
         (toStringInfix [this] (if (= (count args) 1) (str sign "(" (.toStringInfix (first args)) ")")
                                                      (str "(" (clojure.string/join (str " " sign " ") (map #(.toStringInfix %) args)) ")")))
         (diff [this cur_var] (dff (vector args (mapv #(.diff % cur_var) args)))))

(defn createExpression [func sign dff]
      #(abstractExpression. func sign dff %&))

(defn simpleDiff [func]
      (letfn [(createDiff [func [args diffArgs]] (apply func diffArgs))]
             (partial createDiff func)))

(def Add
  (createExpression +
                    "+"
                    (simpleDiff #(apply Add %&))))
(def Subtract
  (createExpression -
                    "-"
                    (simpleDiff #(apply Subtract %&))))
(def Negate
  (createExpression -
                    "negate"
                    (simpleDiff #(apply Negate %&))))
(def Multiply
  (createExpression *
                    "*"
                    (fn [[args diffArgs]] (second
                                            (reduce (fn [[exprX exprDX] [x dx]]
                                                        (vector (Multiply exprX x)
                                                                (Add (Multiply exprX dx) (Multiply exprDX x))))
                                                    (mapv vector args diffArgs))))))
(def Divide
  (createExpression (fn [a & args] (/ (double a) (apply * args)))
                    "/"
                    (fn [[args diffArgs]] (Divide (second
                                                    (reduce (fn [[exprX exprDX] [x dx]]
                                                                (vector (Multiply exprX x)
                                                                        (Subtract (Multiply exprDX x) (Multiply exprX dx))))
                                                            (mapv vector args diffArgs)))
                                                  (Multiply (apply Multiply (rest args))
                                                            (apply Multiply (rest args)))))))
(def Sum
  (createExpression +
                    "sum"
                    (simpleDiff #(apply Sum %&))))
(def Avg
  (createExpression (fn [& args] (/ (apply + args) (count args)))
                    "avg"
                    (fn [[args diffArgs]] (Divide (apply Sum diffArgs)
                                                  (Constant (count args))))))
(def Pow
  (createExpression #(Math/pow %1 %2)
                    "**"
                    nil))
(def Log
  (createExpression #(/ (Math/log (Math/abs %2))
                        (Math/log (Math/abs %1)))
                    "//"
                    nil))

(def str-to-obj {'+      Add
                 '-      Subtract
                 '*      Multiply
                 '/      Divide
                 'negate Negate
                 'sum    Sum
                 'avg    Avg})

(defn evaluate [a cur_var] (.evaluate a cur_var))
(defn toString [a] (.toString a))
(defn toStringInfix [a] (.toStringInfix a))
(defn diff [a cur_var] (.diff a cur_var))

(defn parse [str-to-expr cur-const cur-variable]
      (fn [stringExpression]
          (letfn [(parseExpression [expression]
                                   (cond (number? expression) (cur-const expression)
                                         (symbol? expression) (cur-variable (str expression))
                                         :else (apply (str-to-expr (first expression))
                                                      (mapv parseExpression (rest expression)))))
                  ] (parseExpression (read-string stringExpression)))))

; (def parseFunction (parse str-to-func constant variable))
(def parseObject (parse str-to-obj Constant Variable))

;HW12
(load-file "parser.clj")

(comment "Simple parsers")
(def *digit (+char "0123456789."))
(def *number (+map read-string (+str (+plus *digit))))
(def *string (fn [s] (+ignore (apply +seq (mapv #(+char (str %1)) s)))))
(def *all-chars (mapv char (range 32 128)))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))
(def *number (+map read-string (+str (+or (+plus *digit)
                                          (+seqf cons *ws (+char "-") (+plus *digit))))))
(def *variable (+str (+plus *letter)))
(comment "Array")
; (defn *array [p]
;       (+seqn 1 (+char "[") p (+char "]")))
; (defn *array [p]
;       (+seqn 1 (+char "[") (+opt p) (+char "]")))
; (defn *array [p]
;       (+seqn 1 (+char "[") (+opt (+seq p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
; (defn *array [p]
;       (+seqn 1 (+char "[") (+opt (+seqf cons p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
; (defn *array [p]
;       (+seqn 1 (+char "[") (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char "]")))
(defn *seq [begin p end]
      (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char end)))
(defn *array [p] (*seq "[" p "]"))

(comment "Objects")
(defn *member [p] (+seq *identifier *ws (+ignore (+char ":")) *ws p))
(defn *object [p] (*seq "{" (*member p) "}"))
(defn *object [p] (+map (partial reduce #(apply assoc %1 %2) {}) (*seq "{" (*member p) "}")))



(comment "Parser")
(def *objects [{"+" Add "-" Subtract},
               {"*" Multiply "/" Divide}
               {"**" Pow "//" Log}])
(declare *parseBinary)

(def *parseUnary #(+or (+map Constant *number)
                       (+map Negate (+seqn 0 (*string "negate") *ws (delay (*parseUnary)) *ws))
                       (+map Variable *variable)
                       (+seqn 1 (+char "(") *ws (delay (*parseBinary 0)) *ws (+char ")") *ws)))
                       
(def *parseBinary
  #(let [*leftFolder (partial reduce (fn [fir [oper sec]] (oper fir sec)))
         *rightFolder (fn [args] (letfn [(recFolder [fir rec-rest]
                                                    (if (empty? rec-rest) fir
                                                      ((first (first rec-rest)) fir (recFolder (second (first rec-rest)) (rest rec-rest)))))]
                                 (recFolder (first args) (rest args))))
         *parseOperation (fn [operations] (apply +or (mapv (fn [[key val]] (+seqf (constantly val) (*string key))) operations)))
         *nextLevel (fn [level] (if (== (+ level 1) (count *objects)) (delay (*parseUnary))
                                                                      (delay (*parseBinary (+ level 1)))))
         *operand (*nextLevel %)
         *getFolder (if (== % 2) *rightFolder *leftFolder)]
    (+seqf (fn[oper rest-exp] (*getFolder (cons oper rest-exp)))
           *operand (+star (+seq *ws (*parseOperation (nth *objects %)) *ws *operand)))))

(def parseObjectInfix (+parser (+seqn 0 *ws (*parseBinary 0) *ws)))
