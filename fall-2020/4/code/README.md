The source code is in the form on .py files with the main.py file calling functions from the other files. For running our code, the folllwing packages are required:

| Library    | Install command                                                                                                                     |
| :----------|:------------------------------------------------------------------------------------------------------------------------------------|
| numpy      | !pip install numpy                                                                                                                  |
| pandas     | !pip install pandas                                                                                                                 |
| matplotlib | !pip install matplotlib                                                                                                             |
| pytorch    | !pip install torch===1.7.1+cu110 torchvision===0.8.2+cu110 torchaudio===0.7.2 -f https://download.pytorch.org/whl/torch_stable.html |

The distributed process is launched with the command in a CLI. The address used is for the Romeo node 1 on the Juliet Futuresystems server:

`python -m torch.distributed.launch --nproc_per_node=8 --nnodes=1 --node_rank=0 --master_addr='172.16.0.141' --master_port='5006' ./main.py --model='baseline' --train_url='train-clean-360'`

We can add an additional command `CUDA_VISIBLE_DEVICES` to set the available GPUs.
