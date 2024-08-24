package io.azraein.inkfx.system.actors.dialogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.azraein.inkfx.system.Paper;

public class DialogueParser {
    public String parse(String dialogueScript) {
        String[] scriptLines = dialogueScript.split("\n");
        Stack<Boolean> conditionStack = new Stack<>();
        StringBuilder dialogueResult = new StringBuilder();
        boolean executing = true;

        for (String line : scriptLines) {
            line = line.trim();
            if (line.startsWith("NPC ->")) {

                if (executing) {
                    String dialogue = line.substring(6).trim();
                    dialogue = dialogue.replace("\"", "");
                    dialogue = replaceVariables(dialogue);
                    dialogueResult.append(dialogue).append("\n");
                }
            } else if (line.startsWith("SCRIPT")) {

                if (executing) {
                    String scriptId = line.substring(6).trim();
                    Paper.SE.runScript(scriptId);
                }
                // Conditionals
            } else if (line.startsWith("IF")) {
                String condition = line.substring(2, line.indexOf("THEN")).trim();
                boolean conditionResult = evaluateCondition(condition);
                executing = executing && conditionResult;
                conditionStack.push(executing);
            } else if (line.startsWith("ELSEIF")) {
                String condition = line.substring(6, line.indexOf("THEN")).trim();
                boolean conditionResult = evaluateCondition(condition);
                executing = !conditionStack.peek() && conditionResult;
            } else if (line.startsWith("ELSE")) {
                executing = !conditionStack.peek();
            } else if (line.startsWith("END")) {
                executing = conditionStack.pop();
            }
        }

        return dialogueResult.toString().trim();
    }

    private boolean evaluateCondition(String condition) {
        // Replace variables in the condition
        condition = replaceVariables(condition);

        // Tokenize the condition with parentheses support
        String[] tokens = tokenizeWithParentheses(condition);
        Stack<Object> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.peek().equals("(")) {
                    processOperator(values, operators.pop());
                }
                operators.pop(); // Remove the '('
            } else if (token.matches("\\$[a-zA-Z_][a-zA-Z0-9_]*")) {
                // This is a variable
                String variableKey = token.substring(1);
                String value = Paper.DATABASE.getGlobal(variableKey).toString();
                values.push(parseValue(value)); // Parse to appropriate type
            } else if (token.equals("!") || token.equals("AND") || token.equals("OR")) {
                // Handle NOT and logical operators
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    processOperator(values, operators.pop());
                }
                operators.push(token);
            } else if (token.equals(">") || token.equals("<") || token.equals(">=") || token.equals("<=")
                    || token.equals("==")) {
                // Comparison operators
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    processOperator(values, operators.pop());
                }
                operators.push(token);
            } else {
                // This is a literal value
                values.push(parseValue(token));
            }
        }

        // Process remaining operators
        while (!operators.isEmpty()) {
            processOperator(values, operators.pop());
        }

        return (boolean) values.pop(); // Ensure the final result is a boolean
    }

    private String[] tokenizeWithParentheses(String condition) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (char c : condition.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else if (c == '(' || c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (c == '>' || c == '<' || c == '=' || c == '!' || c == '&' || c == '|') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                if (c == '=' || c == '!' || c == '>' || c == '<') {
                    token.append(c);
                    if (c == '>' || c == '<') {
                        if (condition.indexOf('=', condition.indexOf(c)) == condition.indexOf(c) + 1) {
                            token.append('=');
                            tokens.add(token.toString());
                            token.setLength(0);
                        }
                    }
                } else {
                    token.append(c);
                    if (c == '&' && condition.indexOf('&', condition.indexOf(c)) == condition.indexOf(c) + 1) {
                        token.append('&');
                        tokens.add(token.toString());
                        token.setLength(0);
                    } else if (c == '|' && condition.indexOf('|', condition.indexOf(c)) == condition.indexOf(c) + 1) {
                        token.append('|');
                        tokens.add(token.toString());
                        token.setLength(0);
                    } else {
                        tokens.add(token.toString());
                        token.setLength(0);
                    }
                }
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }

        return tokens.toArray(new String[0]);
    }

    private Object parseValue(String value) {
        if (value.matches("\\d+\\.\\d+")) {
            return Double.parseDouble(value); // Floating-point numbers
        } else if (value.matches("\\d+")) {
            return Integer.parseInt(value); // Integers
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value); // Boolean literals
        } else {
            return value; // Strings
        }
    }

    private int compare(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return ((Double) left).compareTo((Double) right);
        } else if (left instanceof Integer && right instanceof Integer) {
            return ((Integer) left).compareTo((Integer) right);
        } else if (left instanceof String && right instanceof String) {
            return ((String) left).compareTo((String) right);
        } else {
            throw new IllegalArgumentException("Cannot compare types: " + left.getClass() + " and " + right.getClass());
        }
    }

    private void processOperator(Stack<Object> values, String operator) {
        if (operator.equals("!")) {
            // Handle NOT operation
            Object value = values.pop();
            if (value instanceof Boolean) {
                values.push(!(Boolean) value);
            } else {
                throw new IllegalArgumentException("Cannot apply NOT to non-boolean type: " + value.getClass());
            }
        } else {
            // Handle other operators
            Object right = values.pop();
            Object left = values.pop();

            switch (operator) {
            case "AND":
                values.push((boolean) left && (boolean) right);
                break;
            case "OR":
                values.push((boolean) left || (boolean) right);
                break;
            case ">":
                values.push(compare(left, right) > 0);
                break;
            case "<":
                values.push(compare(left, right) < 0);
                break;
            case ">=":
                values.push(compare(left, right) >= 0);
                break;
            case "<=":
                values.push(compare(left, right) <= 0);
                break;
            case "==":
                values.push(left.equals(right));
                break;
            }
        }
    }

    private int precedence(String operator) {
        switch (operator) {
        case "AND":
            return 1;
        case "OR":
            return 0;
        case ">":
        case "<":
        case ">=":
        case "<=":
        case "==":
            return 2;
        case "!":
            return 3;
        default:
            return -1;
        }
    }

    private String replaceVariables(String text) {
        String[] tokens = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            // Extract any trailing punctuation
            String punctuation = "";
            while (!token.isEmpty() && !Character.isLetterOrDigit(token.charAt(token.length() - 1))) {
                punctuation = token.charAt(token.length() - 1) + punctuation;
                token = token.substring(0, token.length() - 1);
            }

            // Check if the remaining token is a variable
            if (token.matches("\\$[a-zA-Z_][a-zA-Z0-9_]*")) {
                String variableKey = token.substring(1);
                String replacement = Paper.DATABASE.getGlobal(variableKey).toString();
                result.append(replacement).append(punctuation).append(" ");
            } else {
                result.append(token).append(punctuation).append(" ");
            }
        }

        return result.toString().trim();
    }

}
