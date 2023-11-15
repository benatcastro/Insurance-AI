<script>
    import {Button, Col, Container, Form, FormGroup, Input, Label, Row} from "sveltestrap";
    let numberOfEntries = 0;
    let startFrom = 0
    let sourceFile = "insurance.csv"
</script>

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
        <Button color="primary">Get results</Button>
        <Button color="primary">How to use</Button>
        <Button color="primary">More Info</Button>
    </div>
</div>
<slot/>

<style>
    :global(body) {
        margin-left: 10vw;
        margin-right: 10vw;
        margin-top: 5vh;
    }
    h3 {
        margin-bottom: 5rem;
        text-align: center;
    }

    .buttons {
        display: grid;
        grid-template-columns: repeat(3, minmax(300px, 10fr));
        gap: 1rem;
    }
    .input-container {
        align-items: center;
        padding: 20px;
        border-radius: 10px;
        box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
        display: grid;
        grid-template-columns: repeat(3, minmax(200px, 1fr));
        grid-gap: 50px;
    }
    @media (max-width: 900px) {
        .input-container, .buttons {
            grid-template-columns: 1fr;
        }

    }
</style>

