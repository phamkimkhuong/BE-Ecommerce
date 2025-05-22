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
# down container
docker compose -f docker-compose.yml down
# pull images latest
docker compose -f docker-compose.yml pull
# build images
docker compose -f docker-compose.yml up -d
# clean up
docker system prune -f