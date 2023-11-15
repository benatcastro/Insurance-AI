<script>
    import { Button,  Form, FormGroup, Input, Label } from "sveltestrap";
    import DataShowcase from "./DataShowcase.svelte";

    let numberOfEntries = 0;
    let startFrom = 0
    let sourceFile = "insurance.csv"
    let data;

    async function getEntries() {
        const url = new URL("http://localhost:4567");

        console.log("Starting to fetch...");
        data = "loading"
        const response = await fetch(url);

        data = await response.json()
        console.log("response:" + data);
    }
</script>

<main>
    <h3>Request Input configurator</h3>
    <div class="input-container">

        <Form>
            <FormGroup>
                <Label for="Source">Source</Label>
                <Input
                        type="select"
                        name="Some text value"
                        bind:value={sourceFile}
                >
                    <option>File one</option>
                    <option>File two</option>
                    <option>File three</option>
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
                        min={0}
                        max={100}
                        step={5}
                        placeholder="Range placeholder"
                        bind:value={numberOfEntries}
                />

            </FormGroup>
        </Form>
        <Form>
            <FormGroup>
                <Label for="">Start from</Label>
                <Input
                        type="number"
                        name="number"
                        id="startingFromInput"
                        placeholder="Select where you want to start from"
                        bind:value={startFrom}
                />
            </FormGroup>
        </Form>
        <div class="buttons">
            <Button color="primary" on:click={getEntries()}>Get results</Button>
            <Button color="primary">How to use</Button>
            <Button color="primary">More Info</Button>
        </div>
    </div>
    <div class="data-container">
        <DataShowcase {data} ></DataShowcase>

    </div>


</main>
<style>
    main {
        margin-top: 2rem;
        gap: 2rem;
        display: flex;
        align-items: center;
        flex-direction: column;
        height: 100vh;
        width: 100vw;
    }
    h3 {
        margin-bottom: 5rem;
        text-align: center;
    }

    .buttons {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: center;
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
    @media (max-width: 900px) {
        .input-container, .buttons {
            grid-template-columns: 1fr;
        }

    }
</style>
