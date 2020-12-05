extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::fs;

fn parse_passport(passport_data: &Vec<String>) -> HashMap<String, String> {
    let mut passport: HashMap<String, String> = HashMap::new();
    for entry in passport_data.iter() {
        let pair: Vec<&str> = entry.split(':').collect();
        passport.insert(pair[0].to_string(), pair[1].to_string());
    }

    passport
}

/// byr (Birth Year) - four digits; at least 1920 and at most 2002.
/// iyr (Issue Year) - four digits; at least 2010 and at most 2020.
/// eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
/// hgt (Height) - a number followed by either cm or in:
///     If cm, the number must be at least 150 and at most 193.
///     If in, the number must be at least 59 and at most 76.
/// hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
/// ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
/// pid (Passport ID) - a nine-digit number, including leading zeroes.
/// cid (Country ID) - ignored, missing or not.
fn check_passport(passport: &HashMap<String, String>, check_values: bool) -> i32 {
    for mandatory_field in ["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"].iter() {
        if !passport.contains_key(&mandatory_field.to_string()) {
            return 0;
        }
    }

    if !check_values {
        return 1;
    }

    let byr: i32 = passport.get("byr").unwrap().parse().unwrap();
    if byr < 1920 || byr > 2002 {
        return 0;
    }
    let iyr: i32 = passport.get("iyr").unwrap().parse().unwrap();
    if iyr < 2010 || iyr > 2020 {
        return 0;
    }
    let eyr: i32 = passport.get("eyr").unwrap().parse().unwrap();
    if eyr < 2020 || eyr > 2030 {
        return 0;
    }
    let hgt = passport.get("hgt").unwrap();
    let re = Regex::new(r"^(\d+)(cm|in)$").expect("Broken regex");
    if !re.is_match(hgt) {
        return 0;
    }
    let caps = re.captures(hgt).unwrap();
    let n: i32 = (&caps[1]).parse().unwrap();
    let unit: &str = &caps[2];
    if !((unit == "cm" && n >= 150 && n <= 193) || (unit == "in" && n >= 59 && n <= 76)) {
        return 0;
    }

    let hcl = passport.get("hcl").unwrap();
    let re = Regex::new(r"^#[0-9a-f]{6}$").expect("Broken regex");
    if !re.is_match(hcl) {
        return 0;
    }

    let ecl = passport.get("ecl").unwrap();
    let colors = vec!["amb", "blu", "brn", "gry", "grn", "hzl", "oth"];
    if !colors.contains(&ecl.as_str()) {
        return 0;
    }
    let pid = passport.get("pid").unwrap();
    let re = Regex::new(r"^\d\d\d\d\d\d\d\d\d$").expect("Broken regex");
    if !re.is_match(pid) {
        return 0;
    }

    return 1;
}

fn main() {
    // Read data
    let passports: Vec<HashMap<String, String>> = fs::read_to_string("input/04.txt")
        .expect("Can't read input file.")
        .split("\n\n")
        .map(|x| {
            x.trim()
                .to_string()
                .split(|x| x == '\n' || x == ' ')
                .map(|x| x.to_string())
                .collect()
        })
        .map(|x| parse_passport(&x))
        .collect();

    let mut part_1 = 0;
    let mut part_2 = 0;
    for passport in passports.iter() {
        part_1 += check_passport(passport, false);
        part_2 += check_passport(passport, true);
    }
    println!("Part 1: {}", part_1);
    println!("Part 2: {}", part_2);
}
