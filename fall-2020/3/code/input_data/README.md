**Input Data**
The full data was collected by a fleet of 20 autonomous vehicles
over a four-month period which consists of around 1000 hours of
data by Lyft’s level 5 self-driving division. We are choosing a
subset of this data to make it less computationally intensive. It
consists of approximately 44,000 scenes, where each scene is 25
seconds long. The scenes capture the perception output of the selfdriving
system, which encodes the precise positions and motions of
nearby vehicles, cyclists, and pedestrians over time. On top of this,
the dataset contains a high-definition semantic map with 15,242
labelled elements and a high-definition aerial view over the area.
The data is packed in .zarr files and are loaded using the zarr Python
module and are also loaded natively by l5kit. .zarr file contains a
set of:
 Scenes: Driving episodes acquired from a given vehicle for
capturing aerial and semantic views.
 Frames: Snapshots in time of the pose of the vehicle in the form
of time frames.
 Agents: Four agent label probabilities captured by the vehicle's
sensors.
Files in the dataset:
 Aerial_map - an aerial map used when rasterization is performed
 Semantic_map - a high definition semantic map used for
rasterization
 Sample.zarr - a small sample set, designed for exploration
 Train.zarr - the training set, in .zarr format
 Validate.zarr - a validation set
 Test.zarr - test set, in .zarr format
The analysis of train, test and validate data over time is given in the
diagram below:
