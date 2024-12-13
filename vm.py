import subprocess

# Define GPU types to loop through
gpu_types = ["nvidia-tesla-t4"]  # List of GPUs
min_disk_size = 200  # Minimum disk size for better performance
static_ip_name = "vm-static-ip"  # Name for reserved static IP

# Function to get all zones dynamically
def get_all_zones():
    try:
        result = subprocess.run(
            ["gcloud", "compute", "zones", "list", "--format=value(name)"],
            capture_output=True,
            text=True,
            check=True,
        )
        return result.stdout.splitlines()
    except subprocess.CalledProcessError as e:
        print("Error fetching zones:", e)
        return []

# Function to promote ephemeral external IP to static
def promote_to_static_external_ip(instance_name, zone, static_ip_name):
    try:
        # Get the ephemeral IP of the instance
        result = subprocess.run(
            [
                "gcloud", "compute", "instances", "describe", instance_name,
                f"--zone={zone}",
                "--format=value(networkInterfaces[0].accessConfigs[0].natIP)"
            ],
            capture_output=True,
            text=True,
            check=True,
        )
        ephemeral_ip = result.stdout.strip()
        print(f"Ephemeral IP for instance '{instance_name}': {ephemeral_ip}")

        # Promote the ephemeral IP to static
        command = [
            "gcloud", "compute", "addresses", "create", static_ip_name,
            f"--addresses={ephemeral_ip}",
            f"--region={zone.rsplit('-', 1)[0]}"  # Extract region from zone
        ]
        subprocess.run(command, check=True)
        print(f"Promoted ephemeral IP '{ephemeral_ip}' to static with name '{static_ip_name}'.")

    except subprocess.CalledProcessError as e:
        print(f"Error promoting ephemeral IP to static: {e}")

# Main script to iterate over GPU types and zones
def main():
    zones = get_all_zones()
    if not zones:
        print("No zones found. Ensure you are authenticated and have the correct permissions.")
        return

    for gpu_type in gpu_types:
        for zone in zones:
            print(f"Checking GPU: {gpu_type}, Zone: {zone}")

            try:
                # Create VM instance with ephemeral IP
                instance_name = "rag-gpu-vm"
                command = [
                    "gcloud", "compute", "instances", "create", instance_name,
                    f"--accelerator=type={gpu_type},count=1",
                    f"--boot-disk-size={min_disk_size}GB",
                    "--image-family=pytorch-latest-gpu",
                    "--image-project=deeplearning-platform-release",
                    "--zone", zone,
                    "--maintenance-policy=TERMINATE",
                    "--quiet"
                ]
                subprocess.run(command, check=True)
                print(f"VM instance '{instance_name}' created in zone '{zone}'.")

                # Promote the ephemeral IP to static
                promote_to_static_external_ip(instance_name, zone, static_ip_name)
                exit(0)  # Exit the script once a GPU VM is successfully created

            except subprocess.CalledProcessError as e:
                print(f"Combination not available: GPU={gpu_type}, Zone={zone}. Error: {e}")

    print("No suitable GPU and configuration combination available in any zone.")

if __name__ == "__main__":
    main()
