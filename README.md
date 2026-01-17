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

### User Analysis
```bash
gradle run -q --args="'input.csv' 'output.csv' '-a'"
```

### Song Similarity
```bash
gradle run -q --args="'input.csv' 'output.csv' '-u'"
```

### User Predictions
```bash
gradle run -q --args="'input.csv' 'output.csv' '-p'"
```

### Recommendations (K-means)
```bash
gradle run -q --args="'input.csv' 'output.csv' '-r' 'song1' 'song2' 'song3'"
```

### Playlist Generation
```bash
gradle run -q --args="'input.csv' 'output.csv' '-s' 'k' 'song1' 'song2' ..."
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
