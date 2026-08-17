#!/bin/bash

# Install all .Modelfile models in the same directory as this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Installing models from $SCRIPT_DIR..."

# Change to the script's directory to ensure we're working with local files
cd "$SCRIPT_DIR"

# Check if ollama is installed
if ! command -v ollama &> /dev/null; then
    echo "Error: Ollama is not installed or not in PATH"
    exit 1
fi

# Get the list of models that should be installed based on .Modelfile files
models_to_install=()
for file in *.Modelfile; do
    if [[ -f "$file" ]]; then
        # Extract model name from filename (remove .Modelfile extension)
        model_name="${file%.Modelfile}"
        models_to_install+=("$model_name")
    fi
done

# Check if we found any models
if [ ${#models_to_install[@]} -eq 0 ]; then
    echo "No .Modelfile files found in $SCRIPT_DIR"
    exit 1
fi

echo "Found models to install: ${models_to_install[*]}"

# Install each model
for model_name in "${models_to_install[@]}"; do
    file="${model_name}.Modelfile"
    
    if [[ -f "$file" ]]; then
        echo "Installing $model_name..."
        
        # Use ollama create command to create the model
        if ollama create "$model_name" -f "$file"; then
            echo "Successfully installed $model_name"
        else
            echo "Failed to install $model_name"
            exit 1
        fi
    else
        echo "Warning: $file not found"
    fi
done

# Audit the models by checking if they exist in ollama
echo "Auditing installed models..."
for model_name in "${models_to_install[@]}"; do
    echo "Checking $model_name..."
    if ollama list | grep -q "$model_name"; then
        echo "✓ Model $model_name is properly installed"
    else
        echo "✗ Model $model_name is NOT installed correctly"
        exit 1
    fi
done

# Update opencode.json configuration to match the installed models
OPENCODE_CONFIG="$SCRIPT_DIR/../.opencode/opencode.json"

if [[ -f "$OPENCODE_CONFIG" ]]; then
    echo "Updating OpenCode configuration..."
    
    # Create a temporary file with the updated config
    TEMP_CONFIG=$(mktemp)
    
    # Build the models object dynamically based on what we found in the directory
    if [ ${#models_to_install[@]} -eq 1 ]; then
        # Only one model: keep the model attribute pointing to that model
        jq --arg model "${models_to_install[0]}" '
            .provider.ollama.models = {
                ($model): {
                    "name": $model,
                    "tools": true
                }
            } | 
            .model = "ollama/" + $model
        ' "$OPENCODE_CONFIG" > "$TEMP_CONFIG"
    else
        # Multiple models: build the models block dynamically from directory files
        # Use jq to create a new models object with all discovered models
        jq '
            .provider.ollama.models = {}
        ' "$OPENCODE_CONFIG" > "$TEMP_CONFIG"
        
        # Add each model to the models object
        for model_name in "${models_to_install[@]}"; do
            jq --arg model "$model_name" '.provider.ollama.models += {($model): {"name": $model, "tools": true}}' "$TEMP_CONFIG" > "${TEMP_CONFIG}.tmp" && mv "${TEMP_CONFIG}.tmp" "$TEMP_CONFIG"
        done
    fi
    
    # Check if the update was successful
    if [ $? -eq 0 ]; then
        # Apply the changes
        mv "$TEMP_CONFIG" "$OPENCODE_CONFIG"
        echo "✓ OpenCode configuration updated successfully"
    else
        echo "✗ Failed to update OpenCode configuration"
        rm -f "$TEMP_CONFIG"
        exit 1
    fi
    
else
    echo "Warning: OpenCode configuration file not found at $OPENCODE_CONFIG"
fi

echo "Model installation and configuration audit complete."
echo "All models are properly installed and configured for OpenCode usage."