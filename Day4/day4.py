def read_input(filename):
    with open(filename, 'r') as f:
        return [line.strip() for line in f.readlines()]

def is_valid(x, y, rows, cols):
    return 0 <= x < rows and 0 <= y < cols

def check_direction(grid, x, y, dx, dy, rows, cols):
    word = ""
    for i in range(4):  # XMAS is 4 letters
        new_x, new_y = x + i*dx, y + i*dy
        if not is_valid(new_x, new_y, rows, cols):
            return False
        word += grid[new_x][new_y]
    return word == "XMAS"

def find_xmas_in_grid(grid):
    rows = len(grid)
    cols = len(grid[0])
    count = 0
    
    # All 8 directions: right, down-right, down, down-left, left, up-left, up, up-right
    directions = [
        (0, 1), (1, 1), (1, 0), (1, -1),
        (0, -1), (-1, -1), (-1, 0), (-1, 1)
    ]
    
    # Check each starting position
    for i in range(rows):
        for j in range(cols):
            # Try all 8 directions from this position
            for dx, dy in directions:
                if check_direction(grid, i, j, dx, dy, rows, cols):
                    count += 1
    
    return count

def get_diagonal_word(grid, x, y, dx, dy, rows, cols):
    """Get the 3-letter word starting at (x,y) in direction (dx,dy)"""
    if not is_valid(x, y, rows, cols):
        return ""
    word = ""
    for i in range(3):  # MAS is 3 letters
        curr_x, curr_y = x + i*dx, y + i*dy
        if not is_valid(curr_x, curr_y, rows, cols):
            return ""
        word += grid[curr_x][curr_y]
    return word

def find_x_mas_in_grid(grid):
    rows = len(grid)
    cols = len(grid[0])
    count = 0

    # For each potential center point
    for i in range(rows):
        for j in range(cols):
            # Must be an 'A'
            if grid[i][j] != 'A':
                continue

            # Check each diagonal direction
            diagonals = [
                # (start_x, start_y, dx, dy)
                (i-1, j-1, 1, 1),   # top-left to bottom-right
                (i-1, j+1, 1, -1),  # top-right to bottom-left
            ]

            # Get words for both diagonals
            words = []
            for start_x, start_y, dx, dy in diagonals:
                word = get_diagonal_word(grid, start_x, start_y, dx, dy, rows, cols)
                if word in ["MAS", "SAM"]:
                    words.append(True)
                else:
                    words.append(False)

            # If both diagonals form valid words, we found an X-MAS
            if all(words):
                count += 1

    return count

def part1(input_data):
    return find_xmas_in_grid(input_data)

def part2(input_data):
    return find_x_mas_in_grid(input_data)

def main():
    input_data = read_input('Day4/input.txt')
    result1 = part1(input_data)
    print(f"Part 1: XMAS appears {result1} times in the word search")
    
    result2 = part2(input_data)
    print(f"Part 2: X-MAS appears {result2} times in the word search")

if __name__ == "__main__":
    main()