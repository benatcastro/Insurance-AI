<script>
    import {
        Alert,
        Button,
        Form,
        FormGroup,
        Input,
        Label,
        Spinner,
    } from "sveltestrap";
    import DataShowcase from "./DataShowcase.svelte";

    let numberOfEntries = 5;
    let sourceFile = "medical.csv"
    $: responseData = null;

    const url = new URL('http://localhost:4567');

    let howToUseModelOpen = false;

    const toggleHowToUseModal = () => (howToUseModelOpen = !howToUseModelOpen);

    const fetchData = async () => {

        const params = new URLSearchParams();

        params.set("entries", numberOfEntries);

        if (sourceFile === "Medical")
            params.set("file", "medical.csv");
        else
            params.set("file", "mental-health.csv");


        const endpoint = url + "?" + params;

        console.log("starting fecth to ", endpoint);
        try {
            responseData = "loading";
            const response = await fetch(endpoint);
            responseData = await response.json();
            console.log("Fetch finished: ", responseData);
        } catch (error) {
            console.error('Error fetching data:', error);
            responseData = "error";
        }
    };

</script>

<main>
    <div class="introduction">
        <Alert color="light" dismissible heading="Welcome to my demo!">
            <p>When you click on "get results", a request with the selected parameters will be send to the backend</p>
            <p>The backend will parse the input file and use OpenAI api to analyze it, this may take some minutes since it is dependant on external services</p>
            <b>How to use</b>
            <p>Choose the parameters, click "get results" and wait.</p>
            <p><b>Dataset</b>: the dataset that will be analyzed.</p>
            <p><b>Entries</b>: how many of the entries in the dataset will be anylized.</p>

        </Alert>

    </div>
    <div class="input-container">
        <Form>
            <FormGroup>
                <Label for="Dataset">Dataset</Label>
                <Input
                        type="select"
                        name="Some text value"
                        bind:value={sourceFile}
                >
                    <option>Medical</option>
                    <option>Mental Health</option>
                </Input>

            </FormGroup>
        </Form>
        <Form>
            <FormGroup>
                <Label for="">Entries {numberOfEntries}</Label>
                <Input
                        type="range"
                        name="range"
                        id="exampleRange"
                        min={5}
                        max={100}
                        step={5}
                        placeholder="Range placeholder"
                        bind:value={numberOfEntries}
                />

            </FormGroup>
        </Form>
        <Button color="primary"  on:click={fetchData}>Get results</Button>
    </div>
    <div class="data-container">
        {#if (responseData == null)}
            <div class="status-tag">
                <h6>Waiting for request...</h6>
            </div>
        {:else if (responseData === "loading")}
            <div class="status-tag">
                <p>Loading results</p>
                <Spinner></Spinner>
            </div>
        {:else if (responseData === "error")}
            <div class="status-tag">
                <p>Error loading results, please try again</p>
            </div>
        {:else}
            <DataShowcase {responseData} ></DataShowcase>
        {/if}

    </div>

    <h6 style="text-align: center">Made by Beñat Castro</h6>

</main>
<style>

    main {
        margin-top: 2rem;
        gap: 0.5rem;
        display: flex;
        align-items: center;
        flex-direction: column;
        height: 90vh;
        width: 100vw;
    }
    h3 {
        margin-bottom: 2rem;
    }
    .introduction {
        width: 80%;
    }


    .input-container {
        justify-content: center;
        align-items: center;
        width: 80%;
        padding: 20px;
        border-radius: 10px;
        box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
        display: grid;
        grid-template-columns: repeat(3, minmax(100px, 1fr));
        grid-gap: 50px;
    }
    .data-container {
        width: 80%;
    }

    .status-tag {
        display: flex;
        flex-direction: column;
        align-items: center;

    }
    @media (max-width: 900px) {
        .input-container {
            grid-template-columns: 1fr;
        }

    }
</style>
