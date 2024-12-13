# Academic Paper Search and Interaction API

This repository contains a FastAPI-based backend for an academic paper search and interaction application. The application integrates with a university repository database, leveraging embedding models and large language models (LLMs) to provide intelligent search and chat capabilities.

## Features

- **Document Search**: Retrieve related academic documents based on a thesis title.
- **Chat with Documents**: Generate academic answers using a language model, based on paper abstracts and user queries.
- **GPU VM Automation**: Scripts to automate the creation of GPU-enabled VMs on Google Cloud Platform (GCP).
- **Data Initialization**: Scripts to fetch model weights and vector stores from Google Cloud Storage.

## Technologies Used

- **FastAPI**: Backend framework.
- **HuggingFace Transformers**: Tokenizer and LLM integration.
- **ChromaDB**: Persistent vector store for embeddings.
- **Google Cloud Platform**: Deployment and resource provisioning.
- **Pydantic**: Data validation and model management.

---

## Installation

### Prerequisites
- Python 3.9 or later
- Google Cloud SDK installed and authenticated
- Required Python libraries listed in `requirements.txt`
- Access to HuggingFace API token and Google Cloud Storage bucket

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/arulpm018/ipbgptserver.git
   cd ipbgptserver
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Set up environment variables:
   ```bash
   export HF_KEY=<your-huggingface-api-key>
   ```

4. Fetch models and vector stores:
   ```bash
   sudo mkdir -p model vector_store
   sudo gsutil -m cp -r gs://<your-bucket-name>/model/* model/
   sudo gsutil -m cp -r gs://<your-bucket-name>/vector_store/* vector_store/
   ```

5. Start the API:
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8000
   ```

---

## API Endpoints

### 1. **Chat with Document**
   - **Endpoint**: `/chat/`
   - **Method**: `POST`
   - **Request Body**:
     ```json
     {
       "query": "<user query>",
       "context": "<document abstract>",
       "chat_history": [
         {"role": "user", "content": "<previous question>"},
         {"role": "assistant", "content": "<previous response>"}
       ]
     }
     ```
   - **Response**:
     ```json
     {
       "response": "<AI-generated response>"
     }
     ```

### 2. **Retrieve Related Documents**
   - **Endpoint**: `/related_documents/`
   - **Method**: `POST`
   - **Request Body**:
     ```json
     {
       "title": "<thesis title>",
       "number": <number of related documents>
     }
     ```
   - **Response**:
     ```json
     {
       "related_documents": [
         {
           "judul": "<document title>",
           "abstrak": "<abstract snippet>",
           "url": "<document URL>"
         }
       ]
     }
     ```

---

## Deployment

### Using `startup.sh`
1. Execute the startup script to set up the environment and start the server:
   ```bash
   bash startup.sh
   ```

### GCP VM Automation (`vm.py`)
1. Automatically create GPU-enabled VMs by running the script:
   ```bash
   python vm.py
   ```

---

## Folder Structure

```
.
├── main.py           # Main FastAPI application
├── models.py         # Pydantic models for API requests/responses
├── services.py       # Core logic for document chat and retrieval
├── startup.sh        # Startup script for initialization
├── vm.py             # GPU VM automation on GCP
├── requirements.txt  # Python dependencies
```

---

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m 'Add feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Open a Pull Request.

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

## Authors

- Arul PM

---

## Acknowledgments

- HuggingFace for their NLP models and tools.
- Google Cloud for hosting and GPU resources.

