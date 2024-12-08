from itertools import product

def evaluate_equation_part_1(test_value, numbers):
    # Generate all combinations of + and *
    num_operators = len(numbers) - 1
    for ops in product(['+', '*'], repeat=num_operators):
        # Start with the first number
        result = numbers[0]
        # Apply the operators left-to-right
        for i, op in enumerate(ops):
            if op == '+':
                result += numbers[i + 1]
            elif op == '*':
                result *= numbers[i + 1]
        # Check if the result matches the test value
        if result == test_value:
            return True
    return False

def evaluate_equation_part_2(test_value, numbers):
    # Generate all combinations of + and *
    num_operators = len(numbers) - 1
    for ops in product(['+', '*', '||'], repeat=num_operators):
        # Start with the first number
        result = numbers[0]
        # Apply the operators left-to-right
        for i, op in enumerate(ops):
            if op == '+':
                result += numbers[i + 1]
            elif op == '*':
                result *= numbers[i + 1]
            elif op == '||':
                result = int(str(result)+str(numbers[i+1]))
        # Check if the result matches the test value
        if result == test_value:
            return True
    return False

def calculate_total_calibration(input_file):
    total_calibration = 0
    with open(input_file, 'r') as file:
        for line in file:
            # Parse the test value and numbers
            test_value, numbers = line.split(':')
            test_value = int(test_value.strip())
            numbers = list(map(int, numbers.strip().split()))
            # Check if the equation can be made true
            if evaluate_equation_part_2(test_value, numbers):
                total_calibration += test_value
    return total_calibration

# Input file path
input_file = 'Day7/input.txt'

# Calculate and print the total calibration result
total_calibration = calculate_total_calibration(input_file)
print(f"Total Calibration Result Part 1: {total_calibration}")
