//! Solutions for day 18.
extern crate regex;

use regex::Regex;
use std::fs;

/// The representation of a token in an algebraic expression
/// from the input.
#[derive(Debug, Clone, Copy)]
enum Token {
    Number(u64),
    Times,
    Plus,
}

impl Token {
    /// Extract the value from a number-type token.
    fn value(self) -> u64 {
        match self {
            Self::Number(x) => x,
            _ => panic!("Not a number!"),
        }
    }
}

#[doc(hidden)]
fn main() {
    // Read input
    let data: Vec<String> = fs::read_to_string("input/18.txt")
        .expect("Can't read input")
        .lines()
        .map(|line| line.to_string())
        .collect();

    let part_1: u64 = data.iter().map(|line| evaluate(line, false)).sum();
    println!("Part 1: {}", part_1);

    let part_2: u64 = data.iter().map(|line| evaluate(line, true)).sum();
    println!("Part 2: {}", part_2);
}

/// Evaluate the algebraic expression.
///
/// # Arguments
/// * `expression`: The string representing the algebraic expression
/// * `precedence`: Whether or not addition should have precedence over multiplication.
///   The value `false` corresponds to part 1, the value `true` to part 2.
///
/// # Returns
/// `u64`: The value of the expression.
fn evaluate(expression: &str, precedence: bool) -> u64 {
    // Split expression into tokens
    let mut tokens: Vec<Token> = Vec::new();
    // If we encounter a sub-expression in brackets, then `min_pos` will
    // mark the end of this expression and we will skip all intermediate
    // tokens in favour of directly evaluating that sub-expression.
    let mut min_pos = 0;

    let re = Regex::new(r"\d+|[+*]|\(").expect("Bad regex");
    for mat in re.find_iter(expression) {
        if mat.start() < min_pos {
            continue;
        }
        match mat.as_str().chars().next().unwrap() {
            '+' => tokens.push(Token::Plus),
            '*' => tokens.push(Token::Times),
            '(' => {
                let start = mat.start();
                let len = match_bracket(&expression[start..]);
                min_pos = start + len;
                // Remove the brackets around the sub-expression and evaluate it.
                let evaluated = evaluate(&expression[start + 1..min_pos - 1], precedence);
                tokens.push(Token::Number(evaluated));
            }
            // At this point it can only be a number.
            _ => tokens.push(Token::Number(mat.as_str().parse().unwrap())),
        }
    }

    // Evaluate the operator of precedence first (part 2: addition, part 1: all operators)
    let mut new_tokens: Vec<Token> = Vec::new();
    // Buffer the last value and the last operator. They will be flushed to
    // `new_tokens` whenever an operation has to be deferred due to precedence
    // (e.g. multiplication in part 2). Otherwise `last_value` will keep accumulating
    // the results of the action of the operator.
    let mut last_value = tokens[0].value();
    let mut last_op = tokens[1];

    for token in tokens.iter().skip(2) {
        match token {
            // We found a number - look at the last value and operator and
            // decide what to do
            Token::Number(x) => {
                match last_op {
                    // Addition is evaluated immediately
                    Token::Plus => last_value += x,
                    // Without precedence multiplication is evaluated immediately as well,
                    // but with precedence it's deferred by flushing the corresponding value
                    // and operator to `new_tokens`.
                    Token::Times => {
                        if precedence {
                            new_tokens.push(Token::Number(last_value));
                            new_tokens.push(Token::Times);
                            last_value = *x;
                        } else {
                            last_value *= x;
                        }
                    }
                    _ => unreachable!(),
                }
            }
            // If the token wasn't a number, then it's an operator.
            op => last_op = *op,
        }
    }
    new_tokens.push(Token::Number(last_value));
    // At this pint `new_tokens` should be of the form [a, *, b, *, c, ...] so
    // we just need to multiply all values that are there. Note that without
    // precedence (part 1) `new_tokens` will be of the form [a] because all
    // operators will already have been evaluated.
    new_tokens
        .iter()
        .step_by(2)
        .fold(1, |acc, token| acc * token.value())
}

/// Find the position of the matching closing bracket.
///
/// The opening bracket is the first one that is found
/// in the given expression.
///
/// # Arguments
/// `expression`: The expression with brackets
///
/// # Returns
/// `usize`: The position of the closing bracket corresponding to the first
///  opening bracket in the expression.
fn match_bracket(expression: &str) -> usize {
    let mut count: usize = 0;

    for (i, c) in expression.chars().enumerate() {
        match c {
            '(' => count += 1,
            ')' => {
                count -= 1;
                if count == 0 {
                    return i + 1;
                }
            }
            _ => (),
        }
    }
    0
}
