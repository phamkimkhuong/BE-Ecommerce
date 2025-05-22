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

# 1. BUILD VÀ PUSH IMAGES (chỉ trên manager node)
echo "Building and pushing images..."
docker-compose -f docker-compose.yml build
docker-compose -f docker-compose.yml push

# 2. REMOVE STACK CŨ (nếu có)
echo "Removing old stack..."
docker stack rm $STACK_NAME || true

# Đợi stack được remove hoàn toàn
echo "Waiting for stack to be completely removed..."
sleep 10

# 3. DEPLOY STACK MỚI
echo "Deploying new stack..."
docker stack deploy -c docker-compose-swarm.yml $STACK_NAME

# 4. KIỂM TRA STATUS
echo "Checking deployment status..."
docker stack services $STACK_NAME

# 5. CLEAN UP (cẩn thận với lệnh này)
echo "Cleaning up unused resources..."
docker system prune -f  # Bỏ flag -a để không xóa images đang dùng

echo "Deployment completed!"
echo "To check services status: docker service ls"
echo "To check specific service: docker service ps ${STACK_NAME}_<service-name>"