(defn abstractOperation [f] (fn [& args] (fn [vars] (apply f (map #(% vars) args)))))

(def constant constantly)
(defn variable [name] (fn [vars] (vars name)))
(def add (abstractOperation +))
(def subtract (abstractOperation -))
(def multiply (abstractOperation *))
(def negate (abstractOperation -))
(def divide (abstractOperation (fn
  ([arg] (/ 1 (double arg)))
  ([frst & args] (/ (double frst) (reduce * args))))))

(defn sumexp-imp [& args] (reduce + (map (fn [x] (Math/exp x)) args)))
(def sumexp (abstractOperation sumexp-imp))
(def softmax (abstractOperation (fn [& args] (/ (Math/exp (first args)) (apply sumexp-imp args)))))

(def OPERATIONS {
  '+       add
  '-       subtract
  '*       multiply
  '/       divide
  'negate  negate
  'sumexp  sumexp
  'softmax softmax
  })

(defn parse [expression]
  (cond
    (list? expression) (apply (OPERATIONS (first expression)) (map parse (rest expression)))
    (number? expression) (constant expression)
    (symbol? expression) (variable (str expression))))

(defn parseFunction [expression] (parse (read-string expression)))
