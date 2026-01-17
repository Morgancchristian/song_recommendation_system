# Song Recommendation System

A Java-based collaborative filtering application that analyzes user ratings to generate personalized song recommendations.

## Overview

This project implements a complete recommendation system built incrementally across six programming assignments. It processes song rating data, identifies patterns in user preferences, and generates playlists based on song similarity clustering.

## Features

- **Song Statistics**: Calculate rating counts, means, and standard deviations
- **User Profiles**: Generate normalized rating matrices with missing value handling
- **Song Similarity**: Compute Euclidean distance-based similarity between songs
- **User Similarity**: Calculate user-to-user similarity for collaborative filtering
- **Rating Prediction**: Predict missing ratings using similar user data
- **K-means Clustering**: Group similar songs and generate recommendations
- **Playlist Generation**: Create personalized playlists of up to 20 songs

## Requirements

- Java 8+
- Gradle
- Apache Commons CSV 1.10.0
- Apache Commons Math 4.0-beta1

## Installation

```bash
git clone <repository-url>
cd song_recommendation_system
gradle build
```

## Usage

### Basic Song Statistics
```bash
gradle run -q --args="'input.csv' 'output.csv'"
```

**Example:**
```bash
gradle run -q --args="'database/files/file1.csv' 'song_stats.csv'"
```

Input file1.csv:
```csv
song1,user1,3
song1,user2,2
song1,user3,3
song1,user4,5
song2,user1,5
song2,user2,4
song2,user3,2
song2,user4,5
```

Output song_stats.csv:
```csv
song,number of ratings,mean,standard deviation
song1,4,3.25,1.0897247358851685
song2,4,4.0,1.224744871391589
```

### User Analysis
```bash
gradle run -q --args="'input.csv' 'output.csv' '-a'"
```

**Example:**
```bash
gradle run -q --args="'database/files/file4.csv' 'user_analysis.csv' '-a'"
```

Input file4.csv:
```csv
Bohemian Rhapsody,charlie,4
Sweet Home Alabama,alex,4
All Star,alex,3
All Star,charlie,2
Sweet Home Alabama,charlie,3
All Star,cameron,5
```

Output user_analysis.csv:
```csv
username,song,rating
alex,All Star,3
alex,Bohemian Rhapsody,NaN
alex,Sweet Home Alabama,4
charlie,All Star,2
charlie,Bohemian Rhapsody,4
charlie,Sweet Home Alabama,3
```

### Song Similarity
```bash
gradle run -q --args="'input.csv' 'output.csv' '-u'"
```

**Example:**
```bash
gradle run -q --args="'database/files/file2.csv' 'song_similarities.csv' '-u'"
```

Input file2.csv:
```csv
I Gotta Feeling,sifat,1
Big Girls Don't Cry,maxi,4
I Gotta Feeling,maxi,2
Big Girls Don't Cry,sifat,2
Suit & Tie,sifat,5
Big Girls Don't Cry,sri,4
```

Output song_similarities.csv:
```csv
name1,name2,similarity
Big Girls Don't Cry,I Gotta Feeling,2.0847431127488694
Big Girls Don't Cry,Suit & Tie,1.765045216243656
I Gotta Feeling,Suit & Tie,2.353393621658208
```

### User Predictions
```bash
gradle run -q --args="'input.csv' 'output.csv' '-p'"
```

**Example:**
```bash
gradle run -q --args="'database/files/file3.csv' 'predictions.csv' '-p'"
```

Output predictions.csv:
```csv
song,user,predicted rating
song1,user5,2
song2,user3,5
song2,user4,1
song2,user5,NaN
song3,user1,4
song3,user2,5
song3,user5,3
song4,user5,2
song5,user1,4
song5,user2,5
song5,user4,2
song6,user1,NaN
song6,user2,NaN
song6,user3,4
song6,user4,NaN
```

### Recommendations (K-means)
```bash
gradle run -q --args="'input.csv' 'output.csv' '-r' 'song1' 'song2' 'song3'"
```

**Example:**
```bash
gradle run -q --args="'database/files/file3.csv' 'output.csv' '-r' 'song3' 'song5' 'song6'"
```

Output output.csv:
```csv
user choice,recommendation
song3,song4
song6,song1
song6,song2
```

### Playlist Generation
```bash
gradle run -q --args="'input.csv' 'output.csv' '-s' 'k' 'song1' 'song2' ..."
```

**Example:**
```bash
gradle run -q --args="'database/files/rock.csv' 'playlist.csv' '-s' '1' 'Bohemian Rhapsody'"
```

Input rock.csv:
```csv
Bohemian Rhapsody,user1,5
Bohemian Rhapsody,user2,4
Bohemian Rhapsody,user3,5
Stairway to Heaven,user1,5
Stairway to Heaven,user2,4
Stairway to Heaven,user3,5
Hotel California,user1,5
Hotel California,user2,4
Hotel California,user3,3
November Rain,user1,1
November Rain,user2,5
November Rain,user3,1
```

Output playlist.csv:
```
Stairway to Heaven
Hotel California
November Rain
```

## Input Format

CSV files with format: `song,user,rating`

```csv
song1,user1,3
song1,user2,2
song2,user1,5
```

Ratings must be integers from 1-5.

## Data Processing Pipeline

1. **Validation**: Verify CSV format and rating ranges
2. **Filtering**: Remove uncooperative users (only one distinct rating)
3. **Normalization**: Z-score normalize ratings by user
4. **Similarity**: Calculate Euclidean distances
5. **Prediction**: Fill missing ratings from similar users
6. **Clustering**: Group songs via K-means
7. **Output**: Generate recommendations

## Output Formats

### Song Statistics
```csv
song,number of ratings,mean,standard deviation
song1,4,3.25,1.0897247358851685
```

### User Profiles
```csv
username,song,rating
user1,song1,4
user1,song2,NaN
```

### Recommendations
```csv
user choice,recommendation
song1,song6
song2,song4
```

### Playlist
```
song1
song4
song8
```

## Error Handling

The application validates:
- Correct number of arguments
- CSV file existence and format
- Output directory existence
- Rating value ranges (1-5)
- Sufficient cooperative users for similarity calculations
- Valid song selections

Errors are reported to `System.err` with descriptive messages.

## Testing

```bash
gradle test
```

Tests cover file I/O, data parsing, statistical calculations, and algorithm correctness.

## Project Structure

```
src/main/java/
  ├── Cs214Project.java          # Main entry point
  ├── CSVtoListofLists.java      # CSV parsing
  ├── CSVFilePathChecks.java     # File validation
  ├── GenerateStatistics.java    # Core algorithms
  ├── WriteProcDataToCSV.java    # Output writing
  ├── ExecutionPath.java         # Argument routing
  └── Cluster.java               # K-means cluster model

src/test/java/
  └── TestCs214Project.java      # JUnit tests

database/files/                  # Sample CSV data
```

## Key Algorithms

### Welford's Algorithm
Online calculation of standard deviation with single data pass.

### Z-score Normalization
```
normalized = (value - mean) / stdDeviation
```

### Euclidean Distance
```
distance = sqrt(Σ(x₁ - x₂)²)
```

### K-means Clustering
10-iteration algorithm with centroid re-computation each iteration.

## Notes

- All floating-point outputs are unrounded
- NaN values represent missing data
- Predictions are bounded to [1, 5]
- Use relative paths for CSV files to ensure portability
- Developed and tested on Linux systems

## Author
Caleb Christian
