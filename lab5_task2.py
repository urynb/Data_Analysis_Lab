# you should only need these libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# step 1: Load the readings
file_path = ""
df = pd.read_csv(file_path)

# time normalization
df['time_s'] = (df['timestamp'] - df['timestamp'].iloc[0]) / 1000.0

# calculate the magnitude
# magnitude is the square root of X squared + Y squared + Z squared
df['mag'] = np.sqrt(df['x']**2 + df['y']**2 + df['z']**2)

# Edit this
# input a threshold based on the plot you just saw
# this represents "how much" deviation from the baseline (9.8 m/s^2, or the force of "resting gravity")
# you think represents movement 
# don't be worried about accuracy
# remember that your "resting" magnitude is ~10
threshold = 0

# when Magnitude > Threshold 
df['state'] = np.where(df['dev'] > threshold, 'Moving', 'Still')

# Add your code here
# plot a pie chart of state proportions using plt


