package nfinity.nfinity.ntype;

/**
 * Insert description here
 *
 * @author Comic
 * @since 10/07/2016 2016
 */
public enum NOperation {
    Compare_LT,
    Compare_GT,
    Compare_EQ,
    Compare_NQ,
    Add,
    Subtract,
    Multiply,
    Divide,
    Modulus,
    Not,
    Binary_OR,
    Binary_AND,
    Binary_XOR,
    Binary_NOT;

    /**
     * Gets the NOperation for the given string representation of an operator
     * @param operator The string of the operator, without whitespace
     * @return The NOperation that corresponds to the given string
     * @throws IllegalArgumentException
     */
    public static NOperation getOperator(String operator) throws IllegalArgumentException {
        switch(operator) {
            case "<=" : {
                return Compare_LT;
            }
            case ">=" : {
                return Compare_GT;
            }
            case "==" : {
                return Compare_EQ;
            }
            case "!=" : {
                return Compare_NQ;
            }
            case "!" : {
                return Not;
            }
            case "+" : {
                return Add;
            }
            case "-" : {
                return Subtract;
            }
            case "*" : {
                return Multiply;
            }
            case "/" : {
                return Divide;
            }
            case "%" : {
                return Modulus;
            }
            case "|" : {
                return Binary_OR;
            }
            case "&" : {
                return Binary_AND;
            }
            case "^" : {
                return Binary_XOR;
            }
            case "~" : {
                return Binary_NOT;
            }
            default : {
                throw new IllegalArgumentException("No operator found for text : " + operator);
            }
        }
    }
}

