The distributed process is launched with the command:

python -m torch.distributed.launch --nproc_per_node=6 --nnodes=1 --node_rank=0 --master_addr=<ipaddress> --master_port=<port> ./main.py --model='baseline' --train_url='train-clean-360'
