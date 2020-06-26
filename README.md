Czech Boolean Expression
========================

This library allows reading expressions written in Czech language that represent logical or conditional sentences, for example:

    je včas
    není včas
    chyb je 300
    proměnná je větší než 67
    číslo je větší nebo rovno -854
    chyb je více než 78
    počet je větší nebo roven 5 a počet je menší nebo roven 10
    lidí je víc jak 78 a zároveň dětí je míň jak 5
    tučňáků není víc jak 5
    letadel není 54
    podezřelých je více jak 15 a zároveň ignorantů je 7
   
 requirements: Java 11+
   
 Maven
 -----
    <dependency>
  		<groupId>com.github.jalasoft</groupId>
  		<artifactId>czech-bool-expression</artifactId>
  		<version>1.0.0</version>
  	</dependency>
  	
 API
 ---
 
    var expression = new CzechBoolExpression();
    
    //parse an expression and get expression for further evaluation
    BoolExpression expression = expression.parse("panáků je více jak 15 nebo piv je 20");
    
    //prepare context with identifiers
    Context context = StandardMapContext.context()
                        .identifier("panáků", 17)
                        .identifier("piv", 5);
                    
    //evaluate expression with given context  
    boolean result = expression.evaluate(context);
    
    //result == true
    ...
    //evaluate the same expression with another context
 
 Technical details:
 -------
 Context free LL(1) grammar:
 
    1    START        -> COND NEXT
    2    COND         -> UNARY_COND
    3    COND         -> BINARY_COND
    4    UNARY_COND   -> je ident
    5    UNARY_COND   -> není ident
    6    BINARY_COND  -> ident OP ROPERAND
    7    OP           -> je OP2
    8    OP           -> není OP2
    9    OP2          -> e
    10   OP2          -> víc OP2_REST
    11   OP2          -> více OP2_REST
    12   OP2          -> větší OP2_REST
    13   OP2          -> menší OP2_REST
    14   OP2          -> méně OP2_REST
    15   OP2          -> míň OP2_REST
    16   OP2          -> rovno
    17   OP2          -> roven
    18   OP2          -> rovna
    19   OP2          -> stejný jako
    20   OP2          -> stejné jako
    21   OP2_REST     -> než
    22   OP2_REST     -> jak
    23   OP2_REST     -> nebo OR_EQUAL
    24   OR_EQUAL     -> rovno OP2_REST2
    25   OR_EQUAL     -> stejné OP2_REST2
    26   OR_EQUAL     -> stejný OP2_REST2
    27   OP2_REST2    -> e
    28   OP2_REST2    -> jak
    29   OP2_REST2    -> jako
    30   ROPERAND     -> number
    31   ROPERAND     -> ident
    32   NEXT         -> e
    33   NEXT         -> AND COND NEXT
    34  AND           -> a AND_REST
    35  AND_REST      -> e
    36  AND_REST      -> zároveň
    37  NEXT          -> nebo COND NEXT