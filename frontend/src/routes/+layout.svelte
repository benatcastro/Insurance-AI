<script>
    import { Navbar, NavbarBrand, } from "sveltestrap";
</script>

<Navbar color="light">
    <NavbarBrand>AI Risk Handler</NavbarBrand>
</Navbar>
<main>
    <slot></slot>
    <h6 class="footer">Made by Beñat Castro</h6>
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
    .footer {
        position: fixed;
        bottom: 0;
    }
</style>
