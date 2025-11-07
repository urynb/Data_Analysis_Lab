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

# plot magnitude vs time
# in this dataframe, time is stored as time_s
# and magnitude as mag
# save your plot and submit it as feedback for this task.
plt.figure(figsize=(12, 5))
