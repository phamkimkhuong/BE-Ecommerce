# shellcheck disable=SC2034
BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo "Deploying with branch: $BRANCH"

# cd on directory source code
cd app/BE-Ecommerce || exit 1

# Get all branches
git fetch -a

# Checkout main branch
git checkout $BRANCH

# Pull the latest changes
git pull

# ===== DOCKER SWARM DEPLOYMENT =====
# Tên stack
STACK_NAME="ecommerce-stack"

# 2. REMOVE STACK CŨ (nếu có)
echo "Removing old stack..."
docker stack rm $STACK_NAME || true

# Đợi stack được remove hoàn toàn
echo "Waiting for stack to be completely removed..."
sleep 10

# down container
docker compose -f docker-compose-swarm.yml down
# pull images latest
docker compose -f docker-compose-swarm.yml pull

# 3. DEPLOY STACK MỚI
echo "Deploying new stack..."
docker stack deploy -c docker-compose-swarm.yml $STACK_NAME

# 4. KIỂM TRA STATUS
echo "Checking deployment status..."
docker stack services $STACK_NAME

docker system prune -f